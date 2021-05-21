package com.my.entities.users;

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

public class Manager extends User {

    private static final Logger logger = LogManager.getLogger();

    private Manager(String login, String password)
            throws InvalidOperationException {
        super(login, password, Role.MANAGER);
    }

    protected Manager(String login, String password, Role role)
            throws InvalidOperationException {
        super(login, password, role);
    }

    public static Manager newManager(String login, String password)
            throws InvalidOperationException, DBException {
        logger.debug("Creating new Manager with login '{}'", login);
        Manager manager = new Manager(login, password);
        int id = UserManager.addUser(manager);
        manager.setId(id);
        return manager;
    }

    public void assignMaster(Request req, Master master) {
        logger.debug("Assigning Master#{} to Request#{} by Manager#{}",
                master.getId(), req.getId(), getId());
        req.setMaster(master);
        req.submitChanges();
    }

    public void topUpClientWallet(Client client, int amount) {
        logger.debug("Adding sum of money ({}) to Client#{}'s wallet by Manager#{}",
                amount, client.getId(), getId());
        Wallet wallet = WalletManager.getWallet(client.getWalletId());
        wallet.addMoney(amount);
        wallet.submitChanges();
    }

    public void cancelRequest(Request req, String reason) {
        logger.debug("Closing Request#{} by Manager#{}", req.getId(), getId());
        req.setStatus(Request.Status.CANCELLED);
        req.setCancelReason(reason);
        req.submitChanges();
    }

    public void applyRequestFroPayment(Request req) {
        logger.debug("Applying Request#{} for payment by Manager#{}", req.getId(), getId());
        req.setStatus(Request.Status.WAITING_FOR_PAYMENT);
        req.submitChanges();
    }

    public List<Request> getRequestList() {
        return RequestManager.getRequestList();
    }

}
