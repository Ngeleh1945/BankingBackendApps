package org.example.employee.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.enums.EmployeeMessage;
import org.example.repository.employee.EmployeeAccountRepository;
import org.example.repository.employee.EmployeeRepository;
import org.example.service.employee.EmployeeManagementService;
import org.example.service.employee.implementation.EmployeeManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmployeeManagementServiceTest {
  @Mock
  private EmployeeRepository repository;

  @Mock
  private EmployeeAccountRepository accountRepository;

  private EmployeeManagementService service;

  @BeforeEach
  void setUp() {
    service = new EmployeeManagementServiceImpl(repository, accountRepository);
  }

  @Test
  void shouldFailWhenEmployeeNotFound() {
    String employeeId = "notExist";

    when(repository.findById(employeeId)).thenReturn(Optional.empty());

    assertThatThrownBy(() ->
        service.getEmployeeById(employeeId)
    )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(EmployeeMessage.NOT_FOUND.format(employeeId));
  }
}
