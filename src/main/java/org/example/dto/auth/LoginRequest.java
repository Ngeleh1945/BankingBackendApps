package org.example.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Login failed: username cannot be empty!")
    private String username;
    @NotBlank(message = "Login failed: password cannot be empty!")
    private String password;
}
