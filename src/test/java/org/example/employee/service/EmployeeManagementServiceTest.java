package org.example.employee.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.example.constant.BankConstant;
import org.example.dto.employee.AddEmployeeRequest;
import org.example.dto.employee.AddEmployeeResponse;
import org.example.model.employee.Employee;
import org.example.repository.employee.EmployeeAccountRepository;
import org.example.repository.employee.EmployeeRepository;
import org.example.service.employee.implementation.EmployeeManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeManagementServiceTest {

    @Mock
    private EmployeeRepository repository;

    @Mock
    private EmployeeAccountRepository employeeAccountRepository;

    @InjectMocks
    private EmployeeManagementServiceImpl employeeService;

    private AddEmployeeRequest request;
    private Employee dummyEmployee;

    @BeforeEach
    void setUp() {
        request = AddEmployeeRequest.builder()
            .nik("12345")
            .fullName("Ahmad Sukandar")
            .placeOfBirth("Medan")
            .dateOfBirth(LocalDate.parse("1980-08-08"))
            .address("Abcd")
            .phoneNumber("088")
            .position("Teller")
            .bankNames(BankConstant.bankName)
            .accountNumber("2323")
            .npwp("332")
            .build();

        dummyEmployee = new Employee();
        dummyEmployee.setEmployeeId("BRM1");
        dummyEmployee.setNik("1112");
        dummyEmployee.setFullName("Ahmad Sukandar");
    }

    @Test
    void testAddEmployeeSuccess() {
        when(repository.findByNik(request.getNik())).thenReturn(Optional.empty());
        when(repository.findAll()).thenReturn(List.of());
        when(repository.save(any(Employee.class))).thenReturn(dummyEmployee);

        AddEmployeeResponse response = employeeService.addEmployees(request);

        assertThat(response).isNotNull();
        assertThat(response.getFullName()).isEqualTo("Ahmad Sukandar");

        verify(repository, times(1)).save(any(Employee.class));
    }

    @Test
    void testAddEmployeeFailedWhenNikDuplicate() {
        when(repository.findByNik(request.getNik())).thenReturn(Optional.of(dummyEmployee));

        assertThatThrownBy(() -> employeeService.addEmployees(request))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("already exist");

        verify(repository, never()).save(any(Employee.class));
    }
}