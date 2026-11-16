package org.example.repository.employee;

import java.util.Optional;
import org.example.model.employee.RoleAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link RoleAccount} entities used for
 * system access control and user authorization.
 */
public interface RoleAccountRepository extends JpaRepository<RoleAccount, String> {
  /**
   * Retrieves a role account entity based on its description.
   *
   * @param desc the role description to search for
   * @return an {@link Optional} containing the matching {@link RoleAccount}, or empty if not found
   */
  Optional<RoleAccount> findByDescription(String desc);
}
