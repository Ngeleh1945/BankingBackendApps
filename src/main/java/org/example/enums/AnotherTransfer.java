package org.example.enums;

public enum AnotherTransfer {
    SENDERORRECEIVER("CASH"),
    SENDERORRECEIVERDESC("-");

    private final String desc;

    AnotherTransfer(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
