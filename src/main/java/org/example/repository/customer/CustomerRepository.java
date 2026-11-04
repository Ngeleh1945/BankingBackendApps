package org.example.repository.customer;

import java.util.List;
import java.util.Optional;
import org.example.model.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for customer data management operations.
 */
public interface CustomerRepository extends JpaRepository<Customer, String> {
}
