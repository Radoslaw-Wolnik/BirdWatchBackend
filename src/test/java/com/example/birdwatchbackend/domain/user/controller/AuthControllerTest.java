package com.example.birdwatchbackend.domain.user.controller;

import com.example.birdwatchbackend.AbstractIntegrationTest;
import com.example.birdwatchbackend.domain.user.dto.LoginRequest;
import com.example.birdwatchbackend.domain.user.dto.RegisterRequest;
import com.example.birdwatchbackend.domain.user.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class AuthControllerTest extends AbstractIntegrationTest {

    @Autowired
    private RestTestClient client;

    @BeforeEach
    void resetClient() {
        client = client.mutate()
                .defaultHeaders(headers -> {
                    headers.clear();
                    headers.remove("Authorization");
                    headers.remove("Cookie");
                })
                .build();
    }

    @Test
    void shouldRegisterAndGetProfile() {
        // 1. Register
        TokenResponse tokenResponse = client.post()
                .uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RegisterRequest("birder1", "birder1@example.com", "strongPass123"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TokenResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(tokenResponse).isNotNull();
        String token = tokenResponse.accessToken();

        // 2. Access /api/users/me with token
        String profile = client.get()
                .uri("/api/users/me")
                .headers(headers -> headers.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertThat(profile).contains("birder1", "birder1@example.com");
    }

    @Test
    void shouldLoginWithEmailOrUsername() {
        // Register a user first
        client.post()
                .uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RegisterRequest("birder2", "birder2@example.com", "passMustBe8Chars"))
                .exchange()
                .expectStatus().isCreated();

        // Login with email
        TokenResponse emailLoginResponse = client.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LoginRequest("birder2@example.com", "passMustBe8Chars"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TokenResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(emailLoginResponse).isNotNull();
        assertThat(emailLoginResponse.accessToken()).isNotBlank();

        // Login with username
        TokenResponse usernameLoginResponse = client.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LoginRequest("birder2", "passMustBe8Chars"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TokenResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(usernameLoginResponse).isNotNull();
        assertThat(usernameLoginResponse.accessToken()).isNotBlank();
    }

    @Test
    void shouldRejectDuplicateRegistration() {
        RegisterRequest request = new RegisterRequest("birder3", "birder3@example.com", "passMustBe8Chars");

        // First registration succeeds
        client.post()
                .uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .exchange()
                .expectStatus().isCreated();

        // Second registration fails
        client.post()
                .uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldRejectInvalidCredentials() {
        client.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LoginRequest("noone@example.com", "wrong"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}