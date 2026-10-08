package repository;

import jakarta.persistence.EntityManager;
import org.example.model.customer.Customer;
import org.example.repository.customer.implementation.CustomerRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
public class CustomerRepositoryImplMockTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private CustomerRepositoryImpl customerRepository;

    private Customer dummyCustomer;
    private String nik = "2345";
    private String fullName = "Budi Sukarmin";
    private String placeOfBirth = "Papua";
    private LocalDate dateOfBirth = LocalDate.parse("1998-04-22");
    private String phoneNumber = "08121";
    private String gender = "Laki - laki";
    private String identityAddress = "lah";
    private String currentAddress = "Mbuh";
    private String emailAddress = "alah@gmail.com";
    private String nationality = "Indonesia";
    private String maritalStatus = "Lajang";
    private String motherMaidenName = "Sdd";

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        dummyCustomer = new Customer();
        dummyCustomer.setNik(nik);
        dummyCustomer.setFullName(fullName);
        dummyCustomer.setPlaceOfBirth(placeOfBirth);
        dummyCustomer.setDateOfBirth(dateOfBirth);
        dummyCustomer.setPhoneNumber(phoneNumber);
        dummyCustomer.setGender(gender);
        dummyCustomer.setIdentityAddress(identityAddress);
        dummyCustomer.setCurrentAddress(currentAddress);
        dummyCustomer.setEmailAddress(emailAddress);
        dummyCustomer.setNationality(nationality);
        dummyCustomer.setMaritalStatus(maritalStatus);
        dummyCustomer.setMotherMaidenName(motherMaidenName);
    }

    @Test
    void testSaveSuccess(){
        when(entityManager.merge(dummyCustomer)).thenReturn(dummyCustomer);
        customerRepository.save(dummyCustomer);
        verify(entityManager,times(1)).merge(dummyCustomer);
        when(entityManager.find(Customer.class, nik)).thenReturn(dummyCustomer);
    }

    @Test
    void testFindByIdNotFound(){
        String nikk = "11";

        when(entityManager.find(Customer.class, nikk)).thenReturn(null);
        Optional<Customer> result = customerRepository.findByNik(nikk);
        assertFalse(result.isPresent(), "Harusnya data kosong");
        verify(entityManager, times(1)).find(Customer.class, nikk);

    }

    @Test
    void testFindByIdFound(){
        String nikk = "2345";

        when(entityManager.find(Customer.class, nikk)).thenReturn(dummyCustomer);
        Optional<Customer> result = customerRepository.findByNik(nikk);
        assertTrue(result.isPresent(), "Harusnya data ada");
        verify(entityManager, times(1)).find(Customer.class, nikk);
    }
}
