package com.example.scheduling_system.enums;

public enum UserRole {
    ADMIN("ADMIN"), USER("USER");

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }
}