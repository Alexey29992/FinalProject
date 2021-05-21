package com.my.entities.items;

import com.my.constants.TextConstants;
import com.my.exceptions.ExceptionMessages;
import com.my.exceptions.InvalidOperationException;
import com.my.utils.WalletManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

public class Wallet implements Serializable {

    private static final Logger logger = LogManager.getLogger(Wallet.class.getName());

    private int id;
    private int balance = 0;
    private final List<PaymentRecord> paymentHistory = new ArrayList<>();

    private Wallet() {
    }

    public static Wallet newWallet() {
        logger.debug("Creating new Wallet");
        Wallet wallet = new Wallet();
        wallet.id = WalletManager.addWallet(wallet);
        return wallet;
    }

    public void addMoney(int amount) {
        logger.debug("Adding {}$ to Wallet#{}", amount, id);
        balance += amount;
        paymentHistory.add(new PaymentRecord(
                new Date(), amount, TextConstants.PAYMENT_RECORD_ADD_MONEY));
    }

    public void makePayment(int amount, int requestId)
            throws InvalidOperationException {
        logger.debug("Taking {}$ from Wallet#{}", amount, id);
        if (balance < amount) {
            logger.error("Negative balance is not allowed");
            throw new InvalidOperationException(ExceptionMessages.NOT_ENOUGH_MONEY);
        }
        balance -= amount;
        paymentHistory.add(new PaymentRecord(
                new Date(), amount, TextConstants.PAYMENT_RECORD_PAY_MONEY + requestId));
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public List<PaymentRecord> getHistory() {
        return paymentHistory;
    }

    public void submitChanges() {
        logger.debug("Submitting Wallet#{} changes to DB", getId());
        WalletManager.updateWallet(this);
    }

}
