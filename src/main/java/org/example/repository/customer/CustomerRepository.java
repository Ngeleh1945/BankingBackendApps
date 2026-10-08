package org.example.repository.customer;

import org.example.model.customer.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    void save(Customer customer);
    Optional<Customer> findByNik(String nik);
    List<Customer> findAll();
    void delete(String nik);
}
