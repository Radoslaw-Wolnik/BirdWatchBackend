package pl.wolnikradoslaw.birdwatchbackend.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Size(min = 3, max = 50)
        @Pattern(regexp = "^[^@]+$", message = "Username must not contain @")
        String username,

        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password
) {}