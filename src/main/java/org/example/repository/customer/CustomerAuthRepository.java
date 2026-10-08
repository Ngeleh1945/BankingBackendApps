package org.example.repository.customer;

import org.example.model.customer.UserCredential;

import java.util.Optional;

public interface CustomerAuthRepository {
    void save(UserCredential userCredential);
    Optional<UserCredential> findByUsername(String username);
    void delete(String accountId);
}
