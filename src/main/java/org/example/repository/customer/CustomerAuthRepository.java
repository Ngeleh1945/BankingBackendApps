package org.example.repository.customer;

import java.util.Optional;
import org.example.model.customer.UserCredential;

/**
 * Repository interface for managing customer authentication data and credentials.
 */
public interface CustomerAuthRepository {

  /**
   * Saves or updates user authentication credentials.
   *
   * @param userCredential the user credential entity to save or update
   * @return the saved user credential instance
   */
  UserCredential save(UserCredential userCredential);

  /**
   * Finds user credentials by username.
   *
   * @param username the username to search for
   * @return an Optional containing the UserCredential if found, or empty otherwise
   */
  Optional<UserCredential> findByUsername(String username);

  /**
   * Deletes user credentials by account ID.
   *
   * @param accountId the unique account ID to delete
   */
  void delete(String accountId);
}
