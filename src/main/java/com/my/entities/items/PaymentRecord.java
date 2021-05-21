package com.my.entities.items;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PaymentRecord implements Serializable {

    private final LocalDateTime date;
    private final int sum;
    private final String destination;

    public PaymentRecord(int sum, String destination) {
        this.sum = sum;
        this.destination = destination;
        date = LocalDateTime.now();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getSum() {
        return sum;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return date.toString() + sum + destination;
    }

}
