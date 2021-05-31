package com.repairagency.entities.items;

import com.repairagency.entities.Item;

import java.time.LocalDateTime;

public class PaymentRecord extends Item {

    private final int sum;
    private final int walletId;
    private final LocalDateTime date;
    private final String destination;

    public PaymentRecord(int sum, int walletId, String destination) {
        this.sum = sum;
        this.walletId = walletId;
        this.destination = destination;
        date = LocalDateTime.now();
    }

    public PaymentRecord(int id, int sum, int walletId, LocalDateTime date, String destination) {
        this.id = id;
        this.sum = sum;
        this.walletId = walletId;
        this.date = date;
        this.destination = destination;
    }

    public int getSum() {
        return sum;
    }

    public int getWalletId() {
        return walletId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return date.toString() + " " + sum + " " + destination;
    }

}
