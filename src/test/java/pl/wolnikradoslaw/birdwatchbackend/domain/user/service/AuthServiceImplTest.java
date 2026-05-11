package pl.wolnikradoslaw.birdwatchbackend.domain.user.service;

import pl.wolnikradoslaw.birdwatchbackend.common.security.JwtService;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.User;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.dto.RegisterRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.repository.UserRepository;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtService jwtService;
    @InjectMocks
    AuthServiceImpl authService;

    @Test
    void shouldRegisterUser() {
        RegisterRequest req = new RegisterRequest("test", "test@example.com", "pass");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode("pass")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtService.generateToken(any(), eq("test@example.com"), eq("USER")))
                .thenReturn("token");

        var response = authService.register(req);
        assertThat(response.accessToken()).isEqualTo("token");
    }
}