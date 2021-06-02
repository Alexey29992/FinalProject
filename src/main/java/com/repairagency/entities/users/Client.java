package com.repairagency.entities.users;

import com.repairagency.entities.EntityUtils;
import com.repairagency.entities.Role;
import com.repairagency.entities.User;
import com.repairagency.entities.beans.PaymentRecord;
import com.repairagency.entities.beans.Request;
import com.repairagency.entities.beans.Wallet;
import com.repairagency.exceptions.DBException;
import com.repairagency.exceptions.InvalidOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Client extends User {

    private static final Logger logger = LogManager.getLogger();

    private Wallet wallet;

    public Client(String login, String password) {
        super(login, password, Role.CLIENT);
    }

    public Client(int id, String login, String password) {
        this(login, password);
        setId(id);
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void createRequest(String description)
            throws DBException {
        logger.debug("Creating new Request for User#{}", getId());
        EntityUtils.newRequest(description, getId());
    }

    public void payForRequest(Request request)
            throws InvalidOperationException, DBException {
        logger.debug("Processing payment for Request#{} from User#{} with Wallet#{}",
                request.getId(), getId(),wallet.getId());
        EntityUtils.walletMakePayment(wallet, request);
    }

    public void addReview(Request request, String review) throws DBException {
        logger.debug("Applying review for Request#{} from User#{}", request.getId(), getId());
        request.setUserReview(review);
        EntityUtils.updateRequest(request);
    }

    public List<Request> getRequestList(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        return EntityUtils.requestGetByClient(getId(), chunkSize, chunkNumber, sortingFactor);
    }

    public List<PaymentRecord> getPaymentHistory(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        return EntityUtils.paymentRecordGetByWallet(wallet.getId(), chunkSize, chunkNumber, sortingFactor);
    }

}
