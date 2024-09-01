package com.tutorial.ecommerce.bankingserviceram.model;

public enum Role {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");
    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return this.roleName;
    }
}
