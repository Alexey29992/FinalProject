package com.repairagency.entities.beans;

import com.repairagency.entities.PersistentEntity;

import java.time.LocalDateTime;

public class PaymentRecord extends PersistentEntity {

    private int sum;
    private int walletId;
    private LocalDateTime date;
    private String destination;

    public PaymentRecord() {
    }

    public PaymentRecord(int sum, int walletId, String destination) {
        this.sum = sum;
        this.walletId = walletId;
        this.destination = destination;
        date = LocalDateTime.now();
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "PaymentRecord{" +
                super.toString() +
                "sum=" + sum +
                ", walletId=" + walletId +
                ", date=" + date +
                ", destination='" + destination + '\'' +
                '}';
    }

}
