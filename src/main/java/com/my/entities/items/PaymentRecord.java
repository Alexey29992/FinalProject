package com.my.entities.items;

import java.io.Serializable;
import java.util.Date;

public class PaymentRecord implements Serializable {

    private final Date date;
    private final int sum;
    private final String destination;

    public PaymentRecord(Date date, int sum, String destination) {
        this.date = date;
        this.sum = sum;
        this.destination = destination;
    }

    public Date getDate() {
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
