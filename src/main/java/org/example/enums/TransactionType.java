package org.example.enums;

import lombok.Getter;


public enum TransactionType {
    TRANSFER_SESAMA("Transfer Sesama Bank"),
    CASH_WITHDRAWAL("Tarik tunai"),
    CASH_DEPOSIT("Setor tunai"),
    CASH_DEPOSIT_TO("Setor tunai ke rekening lain");


    private final String desc;
    TransactionType(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
