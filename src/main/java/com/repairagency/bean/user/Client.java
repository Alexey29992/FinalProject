package com.repairagency.bean.user;

import com.repairagency.bean.User;

/**
 * Bean that represents Client Role
 */

public class Client extends User {

    private int balance;
    private String phNumber;

    public Client(String login, String password) {
        super(login, password, Role.CLIENT);
    }

    public Client(int id, String login, String password) {
        this(login, password);
        setId(id);
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    @Override
    public String toString() {
        return "Client{" + super.toString() +
                "balance=" + balance +
                ", phNumber='" + phNumber + "'}";
    }

}
