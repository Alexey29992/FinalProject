package com.repairagency.entities.beans;

import com.repairagency.entities.PersistentEntity;

public class Wallet extends PersistentEntity {

    private int balance = 0;

    public Wallet() {
    }

    public Wallet(int id) {
        setId(id);
    }

    public Wallet(int id, int balance) {
        setId(id);
        this.balance = balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                super.toString() +
                "balance=" + balance +
                '}';
    }

}
