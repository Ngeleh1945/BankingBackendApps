package org.example.enums;

import lombok.Getter;


public enum TransactionStatus {
    SUCCESS("TRANSFER SUKSES"),
    FAILED("TRANSFER GAGAL");

    private final String description;

    TransactionStatus(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
