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
public class RegisterRequest {
    @NotBlank(message = "Employee ID tidak boleh kosong")
    private String employeeId;
    @NotBlank(message = "Username tidak boleh kosong")
    private String username;
    @NotBlank(message = "password tidak boleh kosong")
    private String password;
    @NotBlank(message = "role id tidak boleh kosong")
    private String roleId;
}
