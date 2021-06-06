package com.repairagency.entities.users;

import com.repairagency.entities.EntityManager;
import com.repairagency.entities.Role;
import com.repairagency.entities.User;
import com.repairagency.entities.beans.Request;
import com.repairagency.exceptions.DBException;
import com.repairagency.exceptions.InvalidOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Manager extends User {

    private static final Logger logger = LogManager.getLogger();

    public Manager(String login, String password) {
        super(login, password, Role.MANAGER);
    }

    public Manager(int id, String login, String password) {
        this(login, password);
        setId(id);
    }

    protected Manager(String login, String password, Role role) {
        super(login, password, role);
    }

    public void assignMaster(Request request, Master master)
            throws InvalidOperationException, DBException {
        logger.debug("Assigning Master#{} to Request#{} by Manager#{}",
                master.getId(), request.getId(), getId());
        request.setMasterId(master.getId());
        EntityManager.updateRequest(request);
    }

    public void topUpClientBalance(Client client, int amount) throws DBException {
        logger.debug("Adding sum of money ({}) to Client#{}'s balance by Manager#{}",
                amount, client.getId(), getId());
        EntityManager.userGiveMoney(client, amount);
    }

    public void cancelRequest(Request request, String reason)
            throws InvalidOperationException, DBException {
        logger.debug("Closing Request#{} by Manager#{}", request.getId(), getId());
        request.setStatus(Request.Status.CANCELLED);
        request.setCancelReason(reason);
        EntityManager.updateRequest(request);
    }

    public void applyForPayment(Request request)
            throws InvalidOperationException, DBException {
        logger.debug("Applying Request#{} for payment by Manager#{}", request.getId(), getId());
        request.setStatus(Request.Status.WAIT_FOR_PAYMENT);
        EntityManager.updateRequest(request);
    }

    public void confirmPayment(Request request)
            throws InvalidOperationException, DBException {
        logger.debug("Confirming payment for Request#{} by Manager#{}", request.getId(), getId());
        request.setStatus(Request.Status.PAID);
        EntityManager.updateRequest(request);
    }

    public List<Request> getRequestList(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        return EntityManager.requestGetAll(chunkSize, chunkNumber, sortingFactor);
    }

    public List<Request> getOpenRequestList(Request.Status status, int chunkSize,
                                            int chunkNumber, String sortingFactor)
            throws DBException {
        return EntityManager.requestGetByStatus(status, chunkSize, chunkNumber, sortingFactor);
    }

}
