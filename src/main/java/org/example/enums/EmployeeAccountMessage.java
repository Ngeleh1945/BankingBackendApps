package org.example.enums;

public enum EmployeeAccountMessage {
    USERNAME_ALREADY_EXISTS("Account with username %s is already registered"),
    EMPLOYEE_ALREADY_HAS_ACCOUNT("Employee with ID %s already has an account"),
    REGISTER_SUCCESS("Account with username %s has been successfully registered");

    private final String desc;

    EmployeeAccountMessage(String desc) {this.desc = desc;}

    public String getDesc() {
        return desc;
    }

    public String format(Object... args) {
        return String.format(desc, args);
    }
}
