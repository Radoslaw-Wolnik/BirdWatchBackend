package pl.wolnikradoslaw.birdwatchbackend.domain.user.service.impl;

import pl.wolnikradoslaw.birdwatchbackend.common.security.JwtService;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.User;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.UserRole;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.dto.LoginRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.dto.RegisterRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.dto.TokenResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.repository.UserRepository;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    @Override
    public TokenResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already taken");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.USER);
        user = userRepository.save(user);

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        return new TokenResponse(token, "Bearer", 86400L);
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        String identifier = request.usernameOrEmail().trim();

        User user = identifier.contains("@")
                ? userRepository.findByEmail(identifier)
                  .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"))
                : userRepository.findByUsername(identifier)
                  .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        return new TokenResponse(token, "Bearer", 86400L);
    }
}