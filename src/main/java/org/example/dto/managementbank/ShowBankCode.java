package org.example.dto.managementbank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ShowBankCode {
  private String bankName;
}
