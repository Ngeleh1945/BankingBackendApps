package org.example.employee.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
class EmployeeRepositoryTest {
  @Autowired
  private EmployeeRepository repository;

  @Autowired
  private PlatformTransactionManager transactionManager;

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

  @Test
  void shouldFindEmployeeById(){
    Employee employee = createEmployee("BRM20", "11155");

    repository.saveAndFlush(employee);

    var result = repository.findById("BRM20");

    assertThat(result).isPresent();
    assertThat(result.get().getEmployeeId()).isEqualTo("BRM20");
    assertThat(result.get().getFullName()).isEqualTo("Mika");
    assertThat(result.get().getAddress()).isEqualTo("Semarang");
  }

  @Test
  void shouldReturnEmptyWhenEmployeeNotFound() {
    var result = repository.findById("NOT-EXIST");

    assertThat(result).isEmpty();
  }

  @Test
  void shouldUpdateEmployee() {
    Employee employee = createEmployee("BRM20", "11155");

    repository.saveAndFlush(employee);

    Employee existingEmployee = repository.findById("BRM20").orElseThrow();

    existingEmployee.setFullName("Joko");
    existingEmployee.setAddress("Bandung");
    existingEmployee.setPosition("Admin");

    Employee updated = repository.saveAndFlush(existingEmployee);

    assertThat(updated.getEmployeeId()).isEqualTo("BRM20");
    assertThat(updated.getFullName()).isEqualTo("Joko");
    assertThat(updated.getAddress()).isEqualTo("Bandung");
    assertThat(updated.getPosition()).isEqualTo("Admin");

  }

  @Test
  void shouldDeleteEmployee() {
    Employee employee = createEmployee("BRM20", "11155");

    repository.saveAndFlush(employee);

    repository.deleteById("BRM20");
    repository.flush();

    var result = repository.findByNik("11155");

    assertThat(result).isEmpty();
  }

  @Test
  void shouldNotThrowWhenDeletingNonExistingEmployee() {
    assertThatCode(() -> repository.deleteById("NOT-EXIST"))
        .doesNotThrowAnyException();
  }

  @Test
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  void shouldAllowOnlyOneInsertWhenSameIdSubmittedConcurrently() throws Exception {

    String employeeId = "CT" + UUID.randomUUID()
        .toString().replace("-", "");

    CyclicBarrier barrier = new CyclicBarrier(2);

    TransactionTemplate transactionTemplate =
        new TransactionTemplate(transactionManager);

    Employee datas1 = createEmployee(employeeId, "52345");
    Employee datas2 = createEmployee(employeeId, "52344");

    try {
      try (ExecutorService executorService =
               Executors.newFixedThreadPool(2)) {

        Future<Boolean> threadA = executorService.submit(() -> {
          try {
            transactionTemplate.execute(status -> {
              try {
                barrier.await();
              } catch (Exception e) {
                throw new IllegalStateException(e);
              }

              repository.saveAndFlush(datas1);
              return null;
            });

            return true;

          } catch (DataIntegrityViolationException e) {
            return false;
          }
        });

        Future<Boolean> threadB = executorService.submit(() -> {
          try {
            transactionTemplate.execute(status -> {
              try {
                barrier.await();
              } catch (Exception e) {
                throw new IllegalStateException(e);
              }

              repository.saveAndFlush(datas2);
              return null;
            });

            return true;

          } catch (DataIntegrityViolationException e) {
            return false;
          }
        });

        List<Boolean> result = List.of(
            threadA.get(),
            threadB.get()
        );

        assertThat(result).containsExactlyInAnyOrder(true, false);
      }

    } finally {
      repository.deleteById(employeeId);
    }
  }
}
