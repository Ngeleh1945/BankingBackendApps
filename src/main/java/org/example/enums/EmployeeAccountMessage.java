package org.example.enums;

public enum EmployeeAccountMessage {
    USERNAME_ALREADY_EXIST("REGISTER GAGAL: USERNAME SUDAH ADA!"),
    USERNAME_CANNOT_NULL("REGISTER GAGAL: USERNAME TIDAK BOLEH KOSONG!"),
    PASSWORD_CANNOT_NULL("REGISTER GAGAL: PASSWORD TIDAK BOLEH KOSONG"),
    REGISTER_SUCCESS("REGISTER BERHASIL");

    private final String desc;

    EmployeeAccountMessage(String desc) {this.desc = desc;}

    public String getDesc() {
        return desc;
    }
}
