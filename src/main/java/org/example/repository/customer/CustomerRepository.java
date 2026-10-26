package org.example.repository.customer;

import java.util.List;
import java.util.Optional;
import org.example.model.customer.Customer;

/**
 * Repository interface for customer data management operations.
 */
public interface CustomerRepository {

  /**
   * Saves or updates a customer entity.
   *
   * @param customer the customer entity to save or update
   * @return the saved customer instance
   */
  Customer save(Customer customer);

  /**
   * Finds a customer by their National Identification Number (NIK).
   *
   * @param nik the NIK to search for
   * @return an Optional containing the customer if found, or empty otherwise
   */
  Optional<Customer> findByNik(String nik);

  /**
   * Retrieves all customer records from the database.
   *
   * @return a list of all customers
   */
  List<Customer> findAll();

  /**
   * Deletes a customer record by their NIK.
   *
   * @param nik the NIK of the customer to delete
   */
  void delete(String nik);
}
