package org.example.enums;

public enum EmployeeMessage {
    ALREADY_EXIST("KARYAWAN DENGAN ID %s SUDAH TERDAFTAR!"),
    CANNOT_NULL("DATA KARYAWAN ATAU ID TIDAK BOLEH KOSONG!"),
    ADD_SUCCESS("KARYAWAN BERHASIL DITAMBAHKAN!");

    private String desc;

    EmployeeMessage(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
