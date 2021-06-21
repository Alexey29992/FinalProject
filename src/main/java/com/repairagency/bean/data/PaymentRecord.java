package com.repairagency.bean.data;

import com.repairagency.bean.AbstractBean;

import java.time.LocalDateTime;

/**
 * Represents payment in Client's payment history
 */
public class PaymentRecord extends AbstractBean {

    public static final String PAYMENT_RECORD_ADD_MONEY = "text.payment_record.add_money";
    public static final String PAYMENT_RECORD_PAY_MONEY = "text.payment_record.pay_money";

    private int sum;
    private int clientId;
    private LocalDateTime date;
    private String destination;

    public PaymentRecord() {
    }

    public PaymentRecord(int sum, int clientId, String destination) {
        this.sum = sum;
        this.clientId = clientId;
        this.destination = destination;
        date = LocalDateTime.now();
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
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
                ", clientId=" + clientId +
                ", date=" + date +
                ", destination='" + destination + '\'' +
                '}';
    }

}
