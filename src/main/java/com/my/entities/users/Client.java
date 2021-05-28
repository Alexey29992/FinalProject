package com.my.entities.users;

import com.my.entities.EntityUtils;
import com.my.entities.Role;
import com.my.entities.User;
import com.my.entities.items.PaymentRecord;
import com.my.entities.items.Request;
import com.my.entities.items.Wallet;
import com.my.exceptions.DBException;
import com.my.exceptions.InvalidOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Client extends User {

    private static final Logger logger = LogManager.getLogger();

    private Wallet wallet;

    public Client(String login, String password)
            throws InvalidOperationException {
        super(login, password, Role.CLIENT);
    }

    public Client(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void createRequest(String description)
            throws InvalidOperationException, DBException {
        logger.debug("Creating new Request for User#{}", getId());
        EntityUtils.newRequest(description, id);
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
        return EntityUtils.requestGetByClient(id, chunkSize, chunkNumber, sortingFactor);
    }

    public List<PaymentRecord> getPaymentHistory(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        return EntityUtils.paymentRecordGetByWallet(wallet.getId(), chunkSize, chunkNumber, sortingFactor);
    }

}
