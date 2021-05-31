package com.repairagency.entities;

import java.io.Serializable;

public abstract class Visitor implements Serializable {

    // session id here...
    // add some
    private Role role;

    protected Visitor() {
    }

    protected Visitor(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
