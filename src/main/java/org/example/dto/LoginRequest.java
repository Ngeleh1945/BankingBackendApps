package org.example.dto;

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
    @NotBlank(message = "Username tidak boleh kosong!")
    private String username;
    @NotBlank(message = "Password tidak boleh kosong")
    private String password;
}
