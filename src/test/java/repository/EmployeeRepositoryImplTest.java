package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.employee.Employee;
import org.example.repository.employee.implementation.EmployeeRepositoryImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeRepositoryImplTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private EmployeeRepositoryImpl repository;
    private final String TEST_EMPLOYEE_ID = "Mbuh2";
    Employee dummy = new Employee();

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        LocalDate dateOfBirth = LocalDate.parse("1980-08-08");
        dummy.setEmployeeId(TEST_EMPLOYEE_ID);
        dummy.setNik("1112");
        dummy.setFullName("Ahmad Sukandar");
        dummy.setPlaceOfBirth("Medan");
        dummy.setDateOfBirth(dateOfBirth);
        dummy.setAddress("Abcd");
        dummy.setPhoneNumber("088");
        dummy.setPosition("Teller");
        dummy.setBankNames("BRI");
        dummy.setAccountNumber("2323");
        dummy.setNpwp("332");
        dummy.setSalary("500000");
    }

    @Test
    @Order(1)
    @DisplayName("Must success add new employee")
    void testSaveSuccess(){
        when(entityManager.find(Employee.class, TEST_EMPLOYEE_ID)).thenReturn(null);

        Optional<Employee> result = repository.findById(TEST_EMPLOYEE_ID);
        assertFalse(result.isPresent(), "Karyawan harusnya tidak ada");
        dummy.setEmployeeId(null);
        assertDoesNotThrow(()-> repository.save(dummy));
        verify(entityManager, times(1)).persist(dummy);
    }

    @Test
    @Order(2)
    @DisplayName("Must success find employee by ID")
    void testFindByIdSuccess(){
        when(entityManager.find(Employee.class, TEST_EMPLOYEE_ID)).thenReturn(dummy);

        Optional<Employee> result = repository.findById(TEST_EMPLOYEE_ID);
        assertTrue(result.isPresent());
        assertEquals("Ahmad Sukandar", result.get().getFullName());
    }

    @Test
    @Order(3)
    @DisplayName("Must failed find employee by ID")
    void testFindByIdNotFound(){
        when(entityManager.find(Employee.class, TEST_EMPLOYEE_ID)).thenReturn(null);

        Optional<Employee> result = repository.findById(TEST_EMPLOYEE_ID);
        assertFalse(result.isPresent());
    }

    @Test
    @Order(4)
    @DisplayName("Must success find all employees")
    void testFindAllEmployees(){
        List<Employee> expected = List.of(dummy);
        TypedQuery<Employee> mockQuery = mock(TypedQuery.class);

        when(entityManager.createQuery(anyString(), eq(Employee.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(expected);

        List<Employee> result = repository.findAll();
        assertNotNull(result, "Hasil tidak boleh null");
        assertFalse(result.isEmpty(), "Daftar karyawan tidak boleh kosong!");
        assertEquals(1, result.size(), "Jumlah karyawan harus tepat 1");
        assertEquals("Ahmad Sukandar", result.get(0).getFullName());

        verify(entityManager, times(1)).createQuery(anyString(), eq(Employee.class));
        verify(mockQuery, times(1)).getResultList();
    }

    @Test
    @Order(5)
    @DisplayName("Must success delete employee")
    void testDeleteEmployees(){
        when(entityManager.find(Employee.class, TEST_EMPLOYEE_ID)).thenReturn(dummy);

        assertDoesNotThrow(()-> repository.delete(TEST_EMPLOYEE_ID));
        verify(entityManager, times(1)).find(Employee.class, TEST_EMPLOYEE_ID);
        verify(entityManager, times(1)).remove(dummy);
    }

    @Test
    @Order(6)
    @DisplayName("Must success update employees")
    void testUpdateEmployees(){
        when(entityManager.find(Employee.class, TEST_EMPLOYEE_ID)).thenReturn(dummy);

        Optional<Employee> result = repository.findById(TEST_EMPLOYEE_ID);
        assertTrue(result.isPresent(), "Karyawan harusnya ada");

        Employee employeeUpdate = result.get();
        employeeUpdate.setPosition("Helper");
        assertDoesNotThrow(()-> repository.save(employeeUpdate));

        verify(entityManager, times(1)).find(Employee.class, TEST_EMPLOYEE_ID);
        verify(entityManager, times(1)).merge(employeeUpdate);


    }
}
