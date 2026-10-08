package org.example.repository.customer.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.model.customer.BankAccount;
import org.example.repository.customer.BankAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BankAccountRepositoryImpl implements BankAccountRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(BankAccount bankAccount){
        if (bankAccount.getAccountId() == null){
            entityManager.persist(bankAccount);
        } else {
            entityManager.merge(bankAccount);
        }
    }

    @Override
    public Optional<BankAccount> findByAccountNumber(String accountNumber){
        BankAccount bankAccount = entityManager.find(BankAccount.class,accountNumber);
        return Optional.ofNullable(bankAccount);
    }
}
