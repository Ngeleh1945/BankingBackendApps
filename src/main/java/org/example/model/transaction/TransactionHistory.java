package org.example.model.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction_history")
public class TransactionHistory {
    @Id
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "sender_account_number")
    private String senderAccountNumber;
    @Column(name = "sender_bank")
    private String senderBank;
    @Column(name = "receiver_account_number")
    private String receiverAccountNumber;
    @Column(name = "receiver_bank")
    private String receiverBank;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "transaction_type")
    private String transactionType;
    @Column(name = "status")
    private String status;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;
}
