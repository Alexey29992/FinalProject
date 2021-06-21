package com.repairagency.bean.user;

import com.repairagency.bean.User;

/**
 * Bean that represents Manager Role
 */
public class Manager extends User {

    public Manager(String login, String password) {
        super(login, password, Role.MANAGER);
    }

    public Manager(int id, String login, String password) {
        this(login, password);
        setId(id);
    }

    protected Manager(String login, String password, Role role) {
        super(login, password, role);
    }

}
