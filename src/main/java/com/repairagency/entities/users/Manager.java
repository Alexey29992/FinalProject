package com.repairagency.entities.users;

import com.repairagency.entities.EntityUtils;
import com.repairagency.entities.Role;
import com.repairagency.entities.User;
import com.repairagency.entities.items.Request;
import com.repairagency.exceptions.DBException;
import com.repairagency.exceptions.InvalidOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Manager extends User {

    private static final Logger logger = LogManager.getLogger();

    protected Manager() {
    }

    public Manager(String login, String password)
            throws InvalidOperationException {
        super(login, password, Role.MANAGER);
    }

    public Manager(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    protected Manager(String login, String password, Role role)
            throws InvalidOperationException {
        super(login, password, role);
    }

    public void assignMaster(Request request, Master master)
            throws InvalidOperationException, DBException {
        logger.debug("Assigning Master#{} to Request#{} by Manager#{}",
                master.getId(), request.getId(), getId());
        request.setMaster(master.getId());
        EntityUtils.updateRequest(request);
    }

    public void topUpClientWallet(Client client, int amount) throws DBException {
        logger.debug("Adding sum of money ({}) to Client#{}'s wallet by Manager#{}",
                amount, client.getId(), getId());
        EntityUtils.walletAddMoney(client.getWallet(), amount);
    }

    public void cancelRequest(Request request, String reason)
            throws InvalidOperationException, DBException {
        logger.debug("Closing Request#{} by Manager#{}", request.getId(), getId());
        request.setStatus(Request.Status.CANCELLED);
        request.setCancelReason(reason);
        EntityUtils.updateRequest(request);
    }

    public void applyForPayment(Request request)
            throws InvalidOperationException, DBException {
        logger.debug("Applying Request#{} for payment by Manager#{}", request.getId(), getId());
        request.setStatus(Request.Status.WAIT_FOR_PAYMENT);
        EntityUtils.updateRequest(request);
    }

    public void confirmPayment(Request request)
            throws InvalidOperationException, DBException {
        logger.debug("Confirming payment for Request#{} by Manager#{}", request.getId(), getId());
        request.setStatus(Request.Status.PAID);
        EntityUtils.updateRequest(request);
    }

    public List<Request> getRequestList(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        return EntityUtils.requestGetAll(chunkSize, chunkNumber, sortingFactor);
    }

    public List<Request> getOpenRequestList(Request.Status status, int chunkSize,
                                            int chunkNumber, String sortingFactor)
            throws DBException {
        return EntityUtils.requestGetByStatus(status, chunkSize, chunkNumber, sortingFactor);
    }

}
