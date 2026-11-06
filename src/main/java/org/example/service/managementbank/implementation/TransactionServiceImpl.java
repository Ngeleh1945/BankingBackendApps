package org.example.service.managementbank.implementation;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.constant.BankConstant;
import org.example.enums.AnotherTransfer;
import org.example.enums.TransactionMessage;
import org.example.enums.TransactionStatus;
import org.example.enums.TransactionType;
import org.example.model.customer.BankAccount;
import org.example.model.managementbank.TransactionHistory;
import org.example.repository.customer.BankAccountRepository;
import org.example.repository.managementbank.TransactionHistoryRepository;
import org.example.service.managementbank.TransactionService;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing bank transactions.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
  private final BankAccountRepository bankAccountRepository;
  private final TransactionHistoryRepository transactionHistoryRepository;
  private static final String MY_BANK_NAME = BankConstant.bankName;

  @Override
  @Transactional
  public void transfer(String senderAccountNumber, String receiverAccountNumber,
                       BigDecimal amount) {
    BankAccount sender =
        getAccountNumber(senderAccountNumber, TransactionMessage.TRANSFER_FAILED_NOTFOUND.name());
    BankAccount receiver =
        getAccountNumber(receiverAccountNumber, TransactionMessage.TRANSFER_FAILED_NOTFOUND.name());

    deductBalance(sender, amount);
    creditBalance(receiver, amount);
    transferHistory(senderAccountNumber, MY_BANK_NAME, receiverAccountNumber, MY_BANK_NAME, amount,
        TransactionType.TRANSFER_SESAMA.name(), TransactionStatus.SUCCESS.name());
    log.info("Transfer berhasil dari {} ke {}", senderAccountNumber, receiverAccountNumber);
  }

  @Override
  @Transactional
  public void cashWithdrawal(String accountNumber, BigDecimal amount) {
    BankAccount account =
        getAccountNumber(accountNumber, TransactionMessage.CASHWITHDRAWAL_FAILED_NOTFOUND.name());
    deductBalance(account, amount);
    transferHistory(accountNumber, AnotherTransfer.SENDERORRECEIVER.name(), MY_BANK_NAME,
        AnotherTransfer.SENDERORRECEIVERDESC.name(), amount, TransactionType.CASH_WITHDRAWAL.name(),
        TransactionStatus.SUCCESS.name());
    log.info("Tarik tunai berhasil untuk akun: {}", accountNumber);
  }

  @Override
  @Transactional
  public void cashDeposit(String accountNumber, BigDecimal amount) {
    BankAccount account = getAccountNumber(accountNumber, TransactionMessage.CASH_DEPOSIT.name());
    creditBalance(account, amount);
    transferHistory(AnotherTransfer.SENDERORRECEIVER.name(), accountNumber,
        AnotherTransfer.SENDERORRECEIVERDESC.name(), MY_BANK_NAME, amount,
        TransactionType.CASH_DEPOSIT.name(), TransactionStatus.SUCCESS.name());
    log.info("Setor tunai berhasil untuk akun: {}", accountNumber);
  }

  @Override
  @Transactional
  public void cashDepositTo(String accountNumberSender, String accountNumberReceiver,
                            BigDecimal amount) {
    BankAccount receiver =
        getAccountNumber(accountNumberSender, TransactionMessage.CASH_DEPOSIT.name());
    creditBalance(receiver, amount);
    transferHistory(accountNumberSender, MY_BANK_NAME, accountNumberReceiver, MY_BANK_NAME, amount,
        TransactionType.CASH_DEPOSIT_TO.name(), TransactionStatus.SUCCESS.name());
    log.info("Setor tunai antar akun berhasil dari {} ke {}", accountNumberSender,
        accountNumberReceiver);
  }

  private BankAccount getAccountNumber(String accountNumber, String message) {
    return bankAccountRepository
        .findById(accountNumber)
        .orElseThrow(() -> new IllegalArgumentException(message));
  }

  private void deductBalance(BankAccount account, BigDecimal amount) {
    if (balanceValidation(account, amount)) {
      throw new IllegalArgumentException(TransactionMessage.INSUFFICIENT_BALANCE.name());
    }
    account.setBalance(account
        .getBalance()
        .subtract(amount));
    bankAccountRepository.save(account);
  }

  private void creditBalance(BankAccount account, BigDecimal amount) {
    account.setBalance(account
        .getBalance()
        .add(amount));
    bankAccountRepository.save(account);
  }

  private boolean balanceValidation(BankAccount bankAccount, BigDecimal amount) {
    BigDecimal minBalance = new BigDecimal("100000");
    return bankAccount
        .getBalance()
        .subtract(amount)
        .compareTo(minBalance) < 0;
  }

  private String generateTransactionId() {
    String uuid = UUID
        .randomUUID()
        .toString()
        .replace("-", "");
    String date = LocalDate
        .now()
        .toString()
        .replace("-", "");
    return "TX - " + date + "-" + uuid.substring(0, 8);
  }

  private void transferHistory(String senderAccountNumber, String receiverAccountNumber,
                               String senderBank, String receiverBank, BigDecimal amount,
                               String type, String status) {
    TransactionHistory mutasi = new TransactionHistory();
    mutasi.setTransactionId(generateTransactionId().toUpperCase());
    mutasi.setSenderAccountNumber(senderAccountNumber);
    mutasi.setSenderBank(senderBank.toUpperCase());
    mutasi.setReceiverAccountNumber(receiverAccountNumber);
    mutasi.setReceiverBank(receiverBank.toUpperCase());
    mutasi.setAmount(amount);
    mutasi.setTransactionType(type.toUpperCase());
    mutasi.setStatus(status.toUpperCase());

    transactionHistoryRepository.save(mutasi);
  }
}