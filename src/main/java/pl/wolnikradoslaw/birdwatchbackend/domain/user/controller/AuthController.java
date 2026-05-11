package pl.wolnikradoslaw.birdwatchbackend.domain.user.controller;

import pl.wolnikradoslaw.birdwatchbackend.domain.user.dto.LoginRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.dto.RegisterRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.dto.TokenResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        TokenResponse token = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok(token);
    }
}
