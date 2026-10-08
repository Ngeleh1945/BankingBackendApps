package org.example.repository.customer.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.model.customer.UserCredential;
import org.example.repository.customer.CustomerAuthRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomerAuthRepositoryImpl implements CustomerAuthRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(UserCredential userCredential){
        if (userCredential.getUsername() == null){
            entityManager.persist(userCredential);
        } else {
            entityManager.merge(userCredential);
        }
    }

    @Override
    public Optional<UserCredential> findByUsername(String username){
        UserCredential userCredential = entityManager.find(UserCredential.class, username);
        return Optional.ofNullable(userCredential);
    }

    @Override
    @Transactional
    public void delete(String accountId){
        UserCredential account = entityManager.find(UserCredential.class, accountId);
        if (account != null) {
            entityManager.remove(account);
        }
    }
}
