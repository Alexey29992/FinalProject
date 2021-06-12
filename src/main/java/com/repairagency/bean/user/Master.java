package com.repairagency.bean.user;

import com.repairagency.bean.User;

public class Master extends User {

    public Master(String login, String password) {
        super(login, password, Role.MASTER);
    }

    public Master(int id, String login, String password) {
        this(login, password);
        setId(id);
    }

}
