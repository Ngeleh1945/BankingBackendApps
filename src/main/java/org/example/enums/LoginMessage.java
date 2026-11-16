package org.example.enums;

public enum LoginMessage {
    USERNAMEANDPASSWORD_ISNULL("LOGIN FAILED: USERNAME AND PASSWORD CANNOT BE EMPTY!"),
    USERPASSWRONG("LOGIN FAILED: INCORRECT USERNAME AND PASSWORD"),
    ACCOUNT_NOTFOUND("LOGIN FAILED: ACCOUNT NOT REGISTERED"),
    WRONGPASSWORD("LOGIN FAILED: INCORRECT PASSWORD"),
    LOGINSUCCESS("LOGIN SUCCESS: WELCOME "),
    INVALIDSESSION("ACCESS DENIED: INVALID LOGIN SESSION!"),
    ACCOUNTWITHEMPLOYEEID_NOT_FOUND("Account not found: Employee account with ID %s not found!"),
    SESSIONACTIVE("LOGIN FAILED: SESSION IS ACTIVE AND CURRENTLY USED BY ANOTHER DEVICE!");

    private final String desc;

    LoginMessage(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String format(Object... args) {
        return String.format(desc, args);
    }
}
