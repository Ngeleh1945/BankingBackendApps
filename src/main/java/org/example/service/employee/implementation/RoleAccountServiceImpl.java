package org.example.service.employee.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.RoleMessage;
import org.example.model.employee.RoleAccount;
import org.example.repository.employee.RoleAccountRepository;
import org.example.service.employee.RoleAccountService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing {@link RoleAccount} business operations.
 * Handles creation, retrieval, and deletion of system roles used for user authorization.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleAccountServiceImpl implements RoleAccountService {
  private final RoleAccountRepository repository;

  /**
   * Registers a new role account into the system after verifying that
   * the role description does not already exist.
   *
   * @param request the {@link RoleAccount} entity containing the new role details
   * @return the successfully persisted {@link RoleAccount} entity
   */
  @Override
  @Transactional
  public RoleAccount addRole(String request) {
    repository
        .findByDescription(request)
        .ifPresent(existingRole -> {
          throw new IllegalStateException(
              RoleMessage.DESCRIPTIONALREADYEXIST.getDesc()
          );
        });

    RoleAccount role = new RoleAccount();
    role.setDescription(request);

    return repository.save(role);
  }

  @Override
  public List<RoleAccount> getAllRole() {
    return repository.findAll();
  }

  @Override
  public Optional<RoleAccount> getRoleDataById(String employeeId) {
    return repository.findById(employeeId);
  }

  @Override
  @Transactional
  public void deleteRole(String desc) {
    RoleAccount roleAccount = repository.findByDescription(desc)
        .orElseThrow(() -> new EntityNotFoundException(RoleMessage.NOT_FOUND.getDesc()));

    repository.delete(roleAccount);
  }

  private @NotNull String generateId() {
    String prefix = "R";

    int lastNumber = repository
        .findAll()
        .stream()
        .map(RoleAccount::getRoleId)
        .filter(id -> id != null && id.matches("^BRM\\d+$"))
        .mapToInt(id -> Integer.parseInt(id.substring(prefix.length())))
        .max()
        .orElse(0);
    return prefix + (lastNumber + 1);
  }
}
