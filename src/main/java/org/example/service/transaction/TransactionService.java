package org.example.service.transaction;

import java.math.BigDecimal;

public interface TransactionService {
    void transfer(String senderAccountNumber, String receiverAccountNumber, BigDecimal amount);
    void cashWithdrawal(String accountNumber, BigDecimal amount);
    void cashDeposit(String accountNumber, BigDecimal amount);
    void cashDepositTo(String accountNumberSender, String accountNumberReceiver, BigDecimal amount);
}
