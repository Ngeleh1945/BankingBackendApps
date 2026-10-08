package org.example.repository.employee.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.model.employee.EmployeeAccount;
import org.example.repository.employee.EmployeeAccountRepository;
import org.example.util.HibernateUtil;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeAccountRepositoryImpl implements EmployeeAccountRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(EmployeeAccount employeeAccount){
        if (employeeAccount.getEmployeeId() == null){
            entityManager.persist(employeeAccount);
        } else {
            entityManager.merge(employeeAccount);
        }
    }

    @Override
    public Optional<EmployeeAccount> findByUsername(String username){
        return entityManager.createQuery("FROM EmployeeAccount e WHERE e.username = :username",EmployeeAccount.class).setParameter("username", username).getResultStream().findFirst();
    }

    @Override
    public List<EmployeeAccount> findAll(){
        return entityManager.createQuery("FROM EmployeeAccount", EmployeeAccount.class).getResultList();
    }


    @Override
    @Transactional
    public void delete(String employeeId){
        EmployeeAccount result = entityManager.find(EmployeeAccount.class, employeeId);
        if (result.getUsername() != null){
            entityManager.remove(result);
        }
    }
}
