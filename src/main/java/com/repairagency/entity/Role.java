package com.repairagency.entity;

public enum Role {

    CLIENT(),
    MASTER(),
    MANAGER(),
    ADMIN();

    public String toLowerCaseString() {
        return super.toString().toLowerCase();
    }

}