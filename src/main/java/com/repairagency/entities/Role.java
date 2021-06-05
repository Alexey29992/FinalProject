package com.repairagency.entities;

public enum Role {

    CLIENT(),
    MASTER(),
    MANAGER(),
    ADMIN();

    public String toLowerCaseString() {
        return super.toString().toLowerCase();
    }

}