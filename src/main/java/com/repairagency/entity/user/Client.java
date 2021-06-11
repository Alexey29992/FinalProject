package com.repairagency.entity.user;

import com.repairagency.entity.EntityManager;
import com.repairagency.entity.User;
import com.repairagency.entity.bean.PaymentRecord;
import com.repairagency.entity.bean.Request;
import com.repairagency.exception.DBException;
import com.repairagency.exception.InvalidOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Client extends User {

    private static final Logger logger = LogManager.getLogger();

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

    public void createRequest(String description)
            throws DBException {
        logger.debug("Creating new Request for User#{}", getId());
        EntityManager.newRequest(description, getId());
    }

    public void payForRequest(Request request)
            throws InvalidOperationException, DBException {
        logger.debug("Processing payment for Request#{} from User#{} with Balance#{}",
                request.getId(), getId(), balance);
        EntityManager.makePayment(this, request.getId());
    }

    public void addReview(Request request, String review) throws DBException {
        logger.debug("Applying review for Request#{} from User#{}", request.getId(), getId());
        request.setUserReview(review);
        EntityManager.updateRequest(request);
    }

    public List<Request> getRequestList(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        return EntityManager.requestGetByClient(getId(), chunkSize, chunkNumber, sortingFactor);
    }

    public List<PaymentRecord> getPaymentHistory(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        return EntityManager.paymentRecordGetByUser(getId(), chunkSize, chunkNumber, sortingFactor);
    }

    @Override
    public String toString() {
        return super.toString() +
                " Client{" +
                "balance=" + balance +
                ", phNumber='" + phNumber + '\'' +
                '}';
    }

}
