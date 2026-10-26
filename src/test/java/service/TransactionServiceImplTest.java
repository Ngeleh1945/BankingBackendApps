package service;

import org.example.model.customer.BankAccount;
import org.example.repository.customer.BankAccountRepository;
import org.example.repository.services.TransactionHistoryRepository;
import org.example.service.transaction.implementation.TransactionServiceImpl;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

public class TransactionServiceImplTest {
    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTransferSuccess(){
        String senderAcc = "112233";
        String receiveAcc = "223344";
        BigDecimal amount = new BigDecimal("500000");

        BankAccount sender = new BankAccount();
        sender.setAccountId("MBUH");
        sender.setAccountNumber(senderAcc);
        sender.setBalance(new BigDecimal("100000"));
        sender.setAccountType("2");

        BankAccount receiver = new BankAccount();
        receiver.setAccountId("MBUH2");
        receiver.setAccountNumber(receiveAcc);
        receiver.setAccountType("3");
        receiver.setBalance(new BigDecimal("50000"));


        when(bankAccountRepository.findByAccountNumber(senderAcc)).thenReturn(Optional.of(sender));
        when(bankAccountRepository.findByAccountNumber(receiveAcc)).thenReturn(Optional.of(receiver));

        assertEquals(new BigDecimal("50000"), sender.getBalance());
        assertEquals(new BigDecimal("70000"), receiver.getBalance());

        verify(transactionHistoryRepository, times(1)).save(any());

    }
}
