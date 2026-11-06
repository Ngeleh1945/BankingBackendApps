package org.example.dto.managementbank;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BankCodeRequest {
  @NotBlank(message = "Code bank cannot be blank")
  private String codeBank;
  @NotBlank(message = "Bank name cannot be blank")
  private String bankName;
  @NotBlank(message = "Swift code cannot be blank")
  private String swiftCode;
}
