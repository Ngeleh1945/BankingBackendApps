package org.example.repository.employee.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.model.employee.Employee;
import org.example.repository.employee.EmployeeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class
EmployeeRepositoryImpl implements EmployeeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(Employee employee){
        if (employee.getEmployeeId() == null){
            entityManager.persist(employee);
        } else {
            entityManager.merge(employee);
        }
    }

    @Override
    public Optional<Employee> findById(String employeeId){
       Employee result = entityManager.find(Employee.class, employeeId);
       return Optional.ofNullable(result);
    }

    @Override
    public List<Employee> findAll(){
        return entityManager.createQuery("FROM Employee", Employee.class).getResultList();
    }

    @Override
    @Transactional
    public void delete(String employeeId){
        Employee result = entityManager.find(Employee.class, employeeId);
        if (result.getEmployeeId() != null){
            entityManager.remove(result);
        }
    }
}
