package org.example.repository.managementbank;

import java.util.List;
import org.example.dto.managementbank.ShowBankCode;
import org.example.model.managementbank.BankCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for managing bank code data operations and database queries.
 */
public interface BankCodeRepository extends JpaRepository<BankCode, String> {

  /**
   * Retrieves a list of all bank codes and bank names projected into a DTO,
   * ordered alphabetically by the bank name.
   *
   * @return a {@link List} of {@link ShowBankCode} containing bank code and name details
   */
  @Query(
      """
        SELECT NEW org.example.dto.managementbank.ShowBankCode(bc.bankName)
        FROM BankCode bc
        ORDER BY bc.bankName
      """
  )
  List<ShowBankCode> findAllBankName();
}