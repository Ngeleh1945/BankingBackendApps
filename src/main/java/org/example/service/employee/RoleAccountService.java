package org.example.service.employee;

import java.util.List;
import java.util.Optional;
import org.example.model.employee.RoleAccount;

/**
 * Service interface for managing {@link RoleAccount} business operations.
 * Handles creation, retrieval, and deletion of system roles used for user authorization.
 */
public interface RoleAccountService {
  /**
   * Registers a new role account into the system.
   *
   * @param request the {@link RoleAccount} entity containing the new role details
   * @return the persisted {@link RoleAccount} entity
   */
  RoleAccount addRole(String request);

  /**
   * Retrieves a comprehensive list of all available role accounts in the system.
   *
   * @return a list of {@link RoleAccount} entities
   */
  List<RoleAccount> getAllRole();

  Optional<RoleAccount> getRoleDataById(String employeeId);

  /**
   * Deletes an existing role account based on its unique role identifier.
   *
   * @param roleId the unique identifier of the role to be removed
   */
  void deleteRole(String roleId);
}
