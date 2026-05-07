package com.example.birdwatchbackend.domain.user.controller;

import com.example.birdwatchbackend.AbstractIntegrationTest;
import com.example.birdwatchbackend.domain.user.dto.RegisterRequest;
import com.example.birdwatchbackend.domain.user.dto.TokenResponse;
import com.example.birdwatchbackend.domain.user.dto.UpdateProfileRequest;
import com.example.birdwatchbackend.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private RestTestClient client;

    @Autowired
    private UserRepository userRepository;

    private String token;

    @BeforeEach
    void setUp() {

        // reset database state
        userRepository.deleteAll();

        // create test user
        TokenResponse tokenResponse = client.post()
                .uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RegisterRequest(
                        "profileuser",
                        "profile@example.com",
                        "passMustBe8Chars"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TokenResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(tokenResponse).isNotNull();

        this.token = tokenResponse.accessToken();
    }

    @Test
    void shouldUpdateProfile() {
        UpdateProfileRequest update = new UpdateProfileRequest(
                "updated_user", "Warsaw, Poland", 52.2297, 21.0122, "Poland", "Mazovia", "Europe/Warsaw");

        String responseBody = client.patch()
                .uri("/api/users/me")
                .headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(update)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertThat(responseBody).contains("updated_user");
        assertThat(responseBody).contains("52.2297");
        assertThat(responseBody).contains("21.0122");
    }
}