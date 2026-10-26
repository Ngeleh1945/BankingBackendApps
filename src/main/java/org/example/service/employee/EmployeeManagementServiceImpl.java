package org.example.service.employee;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.enums.EmployeeMessage;
import org.example.enums.LoginMessage;
import org.example.model.employee.Employee;
import org.example.model.employee.EmployeeAccount;
import org.example.repository.employee.EmployeeAccountRepository;
import org.example.repository.employee.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeManagementServiceImpl implements EmployeeManagementService {
    private final AuthEmployeeServiceImpl authEmployeeServiceImpl;
    private final EmployeeRepository employeeRepository;
    private final EmployeeAccountRepository employeeAccountRepository;

    @Override
    @Transactional
    public boolean addEmployees(Employee employee, String adminId){
        if (adminId == null || adminId.isBlank() )
        {
            throw new IllegalStateException(LoginMessage.INVALIDSESSION.getDesc());
        }

        if(employee == null || employee.getEmployeeId() == null || employee.getEmployeeId().isBlank()){
            throw new IllegalArgumentException(EmployeeMessage.CANNOT_NULL.getDesc());
        }

        Optional<Employee> existingEmployee = employeeRepository.findById(employee.getEmployeeId());
        if(existingEmployee.isPresent()){
            throw new IllegalStateException(String.format(EmployeeMessage.ALREADY_EXIST.getDesc(), employee.getEmployeeId()));
        }

        employeeRepository.save(employee);
        return true;
    }

    @Override
    public List<Employee> getAllEmployees(String adminId) {
        if (adminId == null || adminId.isBlank() )
        {
            throw new IllegalStateException(LoginMessage.INVALIDSESSION.getDesc());
        }

        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(String employeeId){
        return employeeRepository.findById(employeeId).orElseThrow(()-> new IllegalArgumentException("Employee dengan ID " + employeeId + " tidak ditemukan"));
    }
    @Override
    public Employee getEmployeeByUsername(String username){
        EmployeeAccount account = employeeAccountRepository.findByUsername(username).orElseThrow(()-> new IllegalArgumentException("Employee dengan username " + " tidak ditemukan"));
        String employeeId = account.getEmployeeId();
        return employeeRepository.findById(employeeId).orElseThrow(()-> new IllegalArgumentException("Employee dengan ID " + employeeId + " tidak ditemukan"));
    }
}