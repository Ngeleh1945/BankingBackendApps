package repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.example.model.customer.Customer;
import org.example.repository.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomerRepositoryMockTest {

  @Mock
  private CustomerRepository customerRepository;

  private Customer dummyCustomer;

  private final String nik = "2345";
  private final String fullName = "Budi Sukarmin";
  private final String placeOfBirth = "Papua";
  private final LocalDate dateOfBirth = LocalDate.parse("1998-04-22");
  private final String phoneNumber = "08121";
  private final String gender = "Laki - laki";
  private final String identityAddress = "lah";
  private final String currentAddress = "Mbuh";
  private final String emailAddress = "alah@gmail.com";
  private final String nationality = "Indonesia";
  private final String maritalStatus = "Lajang";
  private final String motherMaidenName = "Sdd";

  @BeforeEach
  void setUp() {
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
  void testSaveSuccess() {
    when(customerRepository.save(dummyCustomer))
        .thenReturn(dummyCustomer);

    Customer result = customerRepository.save(dummyCustomer);

    assertNotNull(result);
    assertEquals(nik, result.getNik());

    verify(customerRepository, times(1)).save(dummyCustomer);
  }

  @Test
  void testFindByIdNotFound() {
    String nikk = "11";

    when(customerRepository.findById(nikk))
        .thenReturn(Optional.empty());

    Optional<Customer> result = customerRepository.findById(nikk);

    assertFalse(result.isPresent(), "Harusnya data kosong");

    verify(customerRepository, times(1)).findById(nikk);
  }

  @Test
  void testFindByIdFound() {
    when(customerRepository.findById(nik))
        .thenReturn(Optional.of(dummyCustomer));

    Optional<Customer> result = customerRepository.findById(nik);

    assertTrue(result.isPresent(), "Harusnya data ada");
    assertEquals(fullName, result
        .get()
        .getFullName());

    verify(customerRepository, times(1)).findById(nik);
  }
}