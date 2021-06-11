package com.repairagency.entity;

public abstract class User extends PersistentEntity {

    private Role role;
    private String login;
    private String password;

    public enum Role {

        CLIENT(),
        MASTER(),
        MANAGER(),
        ADMIN();

        public String toLowerCaseString() {
            return super.toString().toLowerCase();
        }

    }

    protected User(String login, String password, Role role) {
        this.role = role;
        this.login = login;
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" + super.toString() + ", role=" + role + ", login='" + login + "'}";
    }

}
