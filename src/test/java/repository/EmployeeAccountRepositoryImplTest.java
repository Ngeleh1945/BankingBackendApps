package repository;

import org.example.model.employee.EmployeeAccount;
import org.example.repository.employee.EmployeeAccountRepository;
import org.example.repository.employee.implementation.EmployeeAccountRepositoryImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeAccountRepositoryImplTest {
    @Mock
    private EmployeeAccountRepository employeeAccountRepository;
    @InjectMocks
    private EmployeeAccountRepositoryImpl employeeAccountRepositoryImpl;
    private final String TEST_EMPLOYEE_ID = "MBUH2";

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    @DisplayName("Add account")
    void testAddAccount(){
        EmployeeAccount employeeAccount = new EmployeeAccount();

        employeeAccount.setEmployeeId(TEST_EMPLOYEE_ID);
        employeeAccount.setUsername("Lohelohe");
        employeeAccount.setPassword("Kepo");
        employeeAccount.setRoleId("1");

        employeeAccountRepository.save(employeeAccount);

        Optional<EmployeeAccount> result = employeeAccountRepository.findByUsername(TEST_EMPLOYEE_ID);
        assertTrue(result.isPresent(), "Akun tidak ada");
        assertEquals("Lohelohe", result.get().getUsername() );
    }

    @Test
    @Order(2)
    @DisplayName("Find by Id")
    void testfindById() {
        Optional<EmployeeAccount> result = employeeAccountRepository.findByUsername(TEST_EMPLOYEE_ID);
        assertEquals("Lohelohe", result.get().getUsername());
        System.out.println("Username : " + result.get().getUsername());
    }

    @Test
    @Order(3)
    @DisplayName("Find All")
    void testFindAll(){
        List<EmployeeAccount> result = employeeAccountRepository.findAll();

        assertFalse(result.isEmpty(),"Harusnya adasi");

        for (EmployeeAccount employeeAccount : result){
            System.out.println(employeeAccount.getEmployeeId() + " ~> " + employeeAccount.getUsername());
        }
    }

    @Test
    @Order(4)
    @DisplayName("Update")
    void testUpdate(){
        Optional<EmployeeAccount> result = employeeAccountRepository.findByUsername(TEST_EMPLOYEE_ID);
        EmployeeAccount employeeAccount = result.get();
        System.out.println(result.get().getUsername() + " " + result.get().getPassword());
        employeeAccount.setPassword("Kepoo");

        assertDoesNotThrow(()->employeeAccountRepository.save(employeeAccount));
        System.out.println(result.get().getUsername() + " " + result.get().getPassword());
    }

    @Test
    @Order(5)
    @DisplayName("Delete")
    void testDelete(){
        assertDoesNotThrow(()-> employeeAccountRepository.delete(TEST_EMPLOYEE_ID));
        Optional<EmployeeAccount> result = employeeAccountRepository.findByUsername(TEST_EMPLOYEE_ID);
        assertFalse(result.isPresent(),"Lah ilang");
    }
}
