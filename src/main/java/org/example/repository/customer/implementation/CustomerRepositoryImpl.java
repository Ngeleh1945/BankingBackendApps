package org.example.repository.customer.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.example.model.customer.Customer;
import org.example.repository.customer.CustomerRepository;
import org.springframework.stereotype.Repository;

/**
 * Implementation of customer data management operations.
 */
@Repository
public class CustomerRepositoryImpl implements CustomerRepository {
  @PersistenceContext
  private EntityManager entityManager;

  @Override
  @Transactional
  public Customer save(Customer customer) {
    if (customer.getNik() == null) {
      entityManager.persist(customer);
      return customer;
    } else {
      return entityManager.merge(customer);
    }
  }

  @Override
  public Optional<Customer> findByNik(String nik) {
    Customer customer = entityManager.find(Customer.class, nik);
    return Optional.ofNullable(customer);
  }

  @Override
  public List<Customer> findAll() {
    return entityManager
        .createQuery("FROM Customer", Customer.class)
        .getResultList();
  }

  @Override
  @Transactional
  public void delete(String nik) {
    Customer result = entityManager.find(Customer.class, nik);
    if (result != null) {
      entityManager.remove(result);
    }
  }
}
