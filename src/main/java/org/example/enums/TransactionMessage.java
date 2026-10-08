package org.example.enums;

public enum TransactionMessage {
    TRANSFER_FAILED_NOTFOUND("TRANSFER GAGAL, NOREK PENGIRIM TIDAK DITEMUKAN"),
    CASHWITHDRAWAL_FAILED_NOTFOUND("TARIK TUNAI GAGAL, NOREK TIDAK VALID!"),
    CASH_DEPOSIT("SETOR TUNAI GAGAL, NOREK TIDAK VALID!"),
    INSUFFICIENT_BALANCE("TRANSAKSI GAGAL, SALDO TIDAK CUKUP!");

    private final String desc;

    TransactionMessage(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
