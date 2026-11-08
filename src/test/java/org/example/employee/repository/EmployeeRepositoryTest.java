package org.example.employee.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.example.constant.BankConstant;
import org.example.model.employee.Employee;
import org.example.repository.employee.EmployeeRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
class EmployeeRepositoryTest {
  @Autowired
  private EmployeeRepository repository;

  @Test
  void shouldSaveEmployee() {
    String employeeId = "BMR10";
    String nik = "12345";
    String fullName = "Mika";
    String placeOfBirth = ("Semarang");
    LocalDate dateOfBirth = LocalDate.of(1965, 6, 19);
    String address = "Semarang";
    String phoneNumber = "+625555565";
    String posisiton = "Staff";
    String bankName = BankConstant.bankName;
    String accountNumber = ("26323232");
    String npwp = ("11212");

    Employee employee = new Employee();
    employee.setEmployeeId(employeeId);
    employee.setNik(nik);
    employee.setFullName(fullName);
    employee.setPlaceOfBirth(placeOfBirth);
    employee.setDateOfBirth(dateOfBirth);
    employee.setAddress(address);
    employee.setPhoneNumber(phoneNumber);
    employee.setPosition(posisiton);
    employee.setBankNames(bankName);
    employee.setAccountNumber(accountNumber);
    employee.setNpwp(npwp);

    Employee saved = repository.saveAndFlush(employee);

    assertThat(saved).isNotNull();
    assertThat(saved.getEmployeeId()).isEqualTo("BMR10");
    assertThat(saved.getNik()).isEqualTo("12345");
    assertThat(saved.getFullName()).isEqualTo("Mika");
  }

  @ParameterizedTest
  @MethodSource("employeeWithNullField")
  void shouldFailWhenRequiredFieldIsNull(Employee employee) {
    assertThatThrownBy(() -> repository.saveAndFlush(employee))
        .isInstanceOf(RuntimeException.class);
  }

  static @NotNull Stream<Employee> employeeWithNullField(){
    return Stream.of(
        createEmployee(null, "12345"),
        createEmployee("BRM10", null)
    );
  }

  private static @NotNull Employee createEmployee(String employeeId, String nik) {
    Employee employee = new Employee();

    employee.setEmployeeId(employeeId);
    employee.setNik(nik);
    employee.setFullName("Mika");
    employee.setPlaceOfBirth("Semarang");
    employee.setDateOfBirth(LocalDate.of(1965, 6, 19));
    employee.setAddress("Semarang");
    employee.setPhoneNumber("+625555565");
    employee.setPosition("Staff");
    employee.setBankNames(BankConstant.bankName);
    employee.setAccountNumber("26323232");
    employee.setNpwp("11212");

    return employee;
  }
}
