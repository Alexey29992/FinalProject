package com.my.entities;

import com.my.exceptions.InvalidOperationException;
import com.my.validators.Validator;

public abstract class User extends Visitor implements PersistentEntity {

    protected int id;
    protected String login;
    protected String password;

    protected User() {
    }

    protected User(String login, String password, Role role)
            throws InvalidOperationException {
        super(role);
        Validator.validateLogin(login);
        this.login = login;
        Validator.validatePassword(password);
        this.password = password;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password)
            throws InvalidOperationException {
        Validator.validatePassword(password);
        this.password = password;
    }

}
