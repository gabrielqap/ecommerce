package com.gabriel.ecommerce.enums;

public enum Role {
    ADMIN("ADMIN"), USER("USER");

    private final String role;

    Role(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.getRole().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No enum constant for role " + role);
    }
}