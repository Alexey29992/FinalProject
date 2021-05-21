package com.my.entities.users;

public enum Role {

    GUEST("Guest"),
    CLIENT("Client"),
    MASTER("Master"),
    MANAGER("Manager"),
    ADMIN("Admin");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

}
