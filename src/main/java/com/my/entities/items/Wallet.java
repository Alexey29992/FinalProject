package com.my.entities.items;

import com.my.entities.Item;

public class Wallet extends Item {

    private int balance = 0;

    public Wallet() {
    }

    public Wallet(int id) {
        this.id = id;
    }

    public Wallet(int id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

}
