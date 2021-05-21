package com.my.entities.users;

import com.my.entities.items.PaymentRecord;
import com.my.entities.items.Request;
import com.my.entities.items.Wallet;
import com.my.exceptions.DBException;
import com.my.exceptions.InvalidOperationException;
import com.my.utils.RequestManager;
import com.my.utils.UserManager;
import com.my.utils.WalletManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Client extends User {

    private static final Logger logger = LogManager.getLogger();

    private int walletId;

    private Client(String login, String password)
            throws InvalidOperationException {
        super(login, password, Role.CLIENT);
    }

    public static Client newClient(String login, String password)
            throws InvalidOperationException, DBException {
        logger.debug("Creating new Client with login '{}'", login);
        Client client = new Client(login, password);
        client.walletId = Wallet.newWallet().getId();
        int id = UserManager.addUser(client);
        client.setId(id);
        return client;
    }

    public void createRequest(String description)
            throws InvalidOperationException {
        logger.debug("Creating new Request for User#{}", getId());
        Request.newRequest(description, this);
    }

    public void payForRequest(Request req)
            throws InvalidOperationException {
        logger.debug("Processing payment for Request#{} from User#{} with Wallet#{}",
                req.getId(), getId(), getWalletId());
        Wallet wallet = WalletManager.getWallet(walletId);
        wallet.makePayment(req.getPrice(), req.getId());
        wallet.submitChanges();
        req.submitChanges();
    }

    public void addReview(Request req, String review) {
        logger.debug("Applying review for Request#{} from User#{}", req.getId(), getId());
        req.setUserReview(review);
        req.submitChanges();
    }

    public List<Request> getRequestList() {
        return RequestManager.getRequestList(this);
    }

    public List<PaymentRecord> getPaymentHistory() {
        Wallet wallet = WalletManager.getWallet(walletId);
        return wallet.getHistory();
    }

    public int getWalletId() {
        return walletId;
    }

}
