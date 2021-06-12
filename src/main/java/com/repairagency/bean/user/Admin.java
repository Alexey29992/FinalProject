package com.repairagency.bean.user;

public class Admin extends Manager {

    public Admin(int id, String login, String password) {
        super(login, password, Role.ADMIN);
        setId(id);
    }

}
