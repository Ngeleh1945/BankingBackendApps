package org.example.enums;

public enum LoginMessage {
    USERNAMEANDPASSWORD_ISNULL("LOGIN GAGAL: USERNAME DAN PASSWORD TIDAK BOLEH KOSONG!"),
    ACCOUNT_NOTFOUND("LOGIN GAGAL: AKUN TIDAK TERDAFTAR"),
    WRONGPASSWORD("LOGIN GAGAL: PASSWORD SALAH"),
    LOGINSUCCESS("LOGIN SUKSES: SELAMAT DATANG "),
    INVALIDSESSION("AKSES DITOLAK: SESI LOGIN TIDAK VALID!");

    private final String desc;

    LoginMessage(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
