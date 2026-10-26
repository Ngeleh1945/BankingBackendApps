package org.example.repository.customer.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.example.model.customer.UserCredential;
import org.example.repository.customer.CustomerAuthRepository;
import org.springframework.stereotype.Repository;

/**
 * Implementation of customer authentication application.
 */
@Repository
public class CustomerAuthRepositoryImpl implements CustomerAuthRepository {
  @PersistenceContext
  private EntityManager entityManager;

  @Override
  @Transactional
  public UserCredential save(UserCredential userCredential) {
    if (userCredential.getUsername() == null) {
      entityManager.persist(userCredential);
      return userCredential;
    } else {
      return entityManager.merge(userCredential);
    }
  }

  @Override
  public Optional<UserCredential> findByUsername(String username) {
    UserCredential userCredential = entityManager.find(UserCredential.class, username);
    return Optional.ofNullable(userCredential);
  }

  @Override
  @Transactional
  public void delete(String accountId) {
    UserCredential account = entityManager.find(UserCredential.class, accountId);
    if (account != null) {
      entityManager.remove(account);
    }
  }
}
