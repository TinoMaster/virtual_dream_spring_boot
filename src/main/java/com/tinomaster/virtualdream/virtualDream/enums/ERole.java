package com.tinomaster.virtualdream.virtualDream.enums;

public enum ERole {
    ADMIN("admin"),
    USER("user"),
    OWNER("owner"),
    EMPLOYEE("employee");

    private final String value;

    ERole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
