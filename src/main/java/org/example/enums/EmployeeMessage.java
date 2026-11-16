package org.example.enums;

public enum EmployeeMessage {
    ALREADY_EXIST("Employee with ID %s is already registered"),
    NOT_FOUND("Employee with ID %s is not found"),
    CANNOT_NULL("Employee data or ID cannot be empty"),
    ADD_SUCCESS("Employee has been successfully added");

    private String desc;

    EmployeeMessage(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String format(Object... args) {
        return String.format(desc, args);
    }
}
