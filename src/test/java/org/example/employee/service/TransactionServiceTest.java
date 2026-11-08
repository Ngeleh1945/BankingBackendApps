package org.example.employee.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import org.example.model.customer.BankAccount;
import org.example.repository.customer.BankAccountRepository;
import org.example.repository.managementbank.TransactionHistoryRepository;
import org.example.service.managementbank.implementation.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TransactionServiceTest {
  @Mock
  private BankAccountRepository bankAccountRepository;

  @Mock
  private TransactionHistoryRepository transactionHistoryRepository;

  @InjectMocks
  private TransactionServiceImpl transactionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testTransferSuccess() {
    String senderAcc = "112233";
    String receiveAcc = "223344";
    BigDecimal amount = new BigDecimal("50000");

    BankAccount sender = new BankAccount();
    sender.setAccountId("MBUH");
    sender.setAccountNumber(senderAcc);
    sender.setBalance(new BigDecimal("150000"));
    sender.setAccountType("2");

    BankAccount receiver = new BankAccount();
    receiver.setAccountId("MBUH2");
    receiver.setAccountNumber(receiveAcc);
    receiver.setAccountType("3");
    receiver.setBalance(new BigDecimal("50000"));


    when(bankAccountRepository.findById(senderAcc)).thenReturn(Optional.of(sender));
    when(bankAccountRepository.findById(receiveAcc)).thenReturn(Optional.of(receiver));

    assertEquals(new BigDecimal("150000"), sender.getBalance());
    assertEquals(new BigDecimal("50000"), receiver.getBalance());

    transactionService.transfer(senderAcc, receiveAcc, amount);

    assertEquals(new BigDecimal("100000"), sender.getBalance());
    assertEquals(new BigDecimal("100000"), receiver.getBalance());

    verify(transactionHistoryRepository, times(1)).save(any());
  }

  @Test
  void testTransferFailed_InsyfficientBalance() {
    String senderAcc = "112233";
    String receiveAcc = "223344";
    BigDecimal amount = new BigDecimal("1500000");

    BankAccount sender = new BankAccount();
    sender.setAccountId("MBUH");
    sender.setAccountNumber(senderAcc);
    sender.setBalance(new BigDecimal("150000"));
    sender.setAccountType("2");

    BankAccount receiver = new BankAccount();
    receiver.setAccountId("MBUH2");
    receiver.setAccountNumber(receiveAcc);
    receiver.setAccountType("3");
    receiver.setBalance(new BigDecimal("50000"));

    assertThrows(IllegalArgumentException.class, () -> {
      transactionService.transfer(senderAcc, receiveAcc, amount);
    });

    verify(transactionHistoryRepository, never()).save(any());
  }

  @Test
  void testTransferFailed_DatabaseErrorOnSave() {
    String senderAcc = "112233";
    String receiveAcc = "223344";
    BigDecimal amount = new BigDecimal("1500000");

    BankAccount sender = new BankAccount();
    sender.setAccountId("MBUH");
    sender.setAccountNumber(senderAcc);
    sender.setBalance(new BigDecimal("150000"));
    sender.setAccountType("2");

    BankAccount receiver = new BankAccount();
    receiver.setAccountId("MBUH2");
    receiver.setAccountNumber(receiveAcc);
    receiver.setAccountType("3");
    receiver.setBalance(new BigDecimal("50000"));

    doThrow(new RuntimeException("Database connection lost"))
        .when(transactionHistoryRepository)
        .save(any());

    assertThrows(RuntimeException.class, () -> {
      transactionService.transfer(senderAcc, receiveAcc, amount);
    });

    verify(transactionHistoryRepository, never()).save(any());
  }
}
