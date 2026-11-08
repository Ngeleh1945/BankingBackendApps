package org.example.employee.repository;

import org.example.model.employee.Employee;
import org.example.repository.employee.EmployeeRepository;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeRepositoryImplTest {

    @Mock
    private EmployeeRepository repository;

    private AutoCloseable mocks;

    private final String TEST_EMPLOYEE_ID = "Mbuh2";

    private Employee dummy;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        dummy = new Employee();

        dummy.setEmployeeId(TEST_EMPLOYEE_ID);
        dummy.setNik("1112");
        dummy.setFullName("Ahmad Sukandar");
        dummy.setPlaceOfBirth("Medan");
        dummy.setDateOfBirth(LocalDate.parse("1980-08-08"));
        dummy.setAddress("Abcd");
        dummy.setPhoneNumber("088");
        dummy.setPosition("Teller");
        dummy.setBankNames("BRI");
        dummy.setAccountNumber("2323");
        dummy.setNpwp("332");
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @Order(1)
    @DisplayName("Must success add new employee")
    void testSaveSuccess() {

        when(repository.save(any(Employee.class)))
            .thenReturn(dummy);

        Employee result = repository.save(dummy);

        assertNotNull(result);
        assertEquals(TEST_EMPLOYEE_ID, result.getEmployeeId());

        verify(repository, times(1)).save(dummy);
    }

    @Test
    @Order(2)
    @DisplayName("Must success find employee by ID")
    void testFindByIdSuccess() {

        when(repository.findById(TEST_EMPLOYEE_ID))
            .thenReturn(Optional.of(dummy));

        Optional<Employee> result =
            repository.findById(TEST_EMPLOYEE_ID);

        assertTrue(result.isPresent());
        assertEquals("Ahmad Sukandar", result.get().getFullName());

        verify(repository, times(1)).findById(TEST_EMPLOYEE_ID);
    }

    @Test
    @Order(3)
    @DisplayName("Must failed find employee by ID")
    void testFindByIdNotFound() {

        when(repository.findById(TEST_EMPLOYEE_ID))
            .thenReturn(Optional.empty());

        Optional<Employee> result =
            repository.findById(TEST_EMPLOYEE_ID);

        assertFalse(result.isPresent());

        verify(repository, times(1)).findById(TEST_EMPLOYEE_ID);
    }

    @Test
    @Order(4)
    @DisplayName("Must success find all employees")
    void testFindAllEmployees() {

        List<Employee> expected = List.of(dummy);

        when(repository.findAll()).thenReturn(expected);

        List<Employee> result = repository.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Ahmad Sukandar", result.get(0).getFullName());

        verify(repository, times(1)).findAll();
    }

    @Test
    @Order(5)
    @DisplayName("Must success delete employee")
    void testDeleteEmployees() {

        doNothing().when(repository).deleteById(TEST_EMPLOYEE_ID);

        assertDoesNotThrow(
            () -> repository.deleteById(TEST_EMPLOYEE_ID)
        );

        verify(repository, times(1)).deleteById(TEST_EMPLOYEE_ID);
    }

    @Test
    @Order(6)
    @DisplayName("Must success update employee")
    void testUpdateEmployees() {

        dummy.setPosition("Helper");

        when(repository.save(dummy)).thenReturn(dummy);

        Employee result = repository.save(dummy);

        assertEquals("Helper", result.getPosition());

        verify(repository, times(1)).save(dummy);
    }
}