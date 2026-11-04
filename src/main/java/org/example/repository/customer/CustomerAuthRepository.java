package org.example.repository.customer;

import java.util.Optional;
import org.example.model.customer.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing customer authentication data and credentials.
 */
public interface CustomerAuthRepository extends JpaRepository<UserCredential, String> {

  /**
   * Finds user credentials by username.
   *
   * @param username the username to search for
   * @return an Optional containing the UserCredential if found, or empty otherwise
   */
  Optional<UserCredential> findByUsername(String username);
}
