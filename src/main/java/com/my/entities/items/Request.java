package com.my.entities.items;

import com.my.entities.users.Client;
import com.my.entities.users.Master;
import com.my.exceptions.ExceptionMessages;
import com.my.exceptions.InvalidOperationException;
import com.my.utils.RequestManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.Date;

public class Request implements Serializable {

    private static final Logger logger = LogManager.getLogger();

    private int id;
    private final int clientId;
    private final Date creationDate;
    private Status status;
    private String description;

    private Date completionDate;
    private String userReview;
    private String cancelReason;
    private int masterId;
    private int price;

    public enum Status {
        NEW("NEW", Type.OPEN),
        WAITING_FOR_PAYMENT("WAITING_FOR_PAYMENT", Type.OPEN),
        PAID("PAID", Type.OPEN),
        CANCELLED("CANCELLED", Type.CLOSED),
        IN_PROCESS("IN_PROCESS", Type.OPEN),
        DONE("DONE", Type.CLOSED);

        public enum Type {
            OPEN,
            CLOSED
        }

        private final String literal;
        private final Type type;

        Status(String literal, Type type) {
            this.type = type;
            this.literal = literal;
        }

        @Override
        public String toString() {
            return literal;
        }

        public Type getType() {
            return type;
        }
    }

    private Request(String description, Client client) {
        this.description = description;
        this.clientId = client.getId();
        status = Status.NEW;
        creationDate = new Date();
    }

    public static Request newRequest(String description, Client client)
            throws InvalidOperationException {
        validateDescription(description);
        logger.debug("Creating new request from Client#{}", client.getId());
        Request req = new Request(description, client);
        req.id = RequestManager.addRequest(req);
        return req;
    }

    public void setStatus(Status status) {
        logger.debug("Setting status of Request#{} to {}", id, status);
        validateMutability();
        this.status = status;
        if (isClosed()) {
            completionDate = new Date();
        }
    }

    public void setCancelReason(String cancelReason) {
        logger.debug("Setting rejection reason of Request#{} to '{}'", id, cancelReason);
        this.cancelReason = cancelReason;
    }

    public void setDescription(String description)
            throws InvalidOperationException {
        logger.debug("Setting description to Request#{} to '{}'", id, description);
        validateDescription(description);
        validateMutability();
        this.description = description;
    }

    public void setUserReview(String userReview) {
        logger.debug("Setting user review to Request#{} to '{}'", id, userReview);
        this.userReview = userReview;
    }

    public void setMaster(Master master) {
        logger.debug("Setting Master#{} to Request#{}", master.getId(), id);
        validateMutability();
        masterId = master.getId();
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void submitChanges() {
        RequestManager.updateRequest(this);
    }

    public int getClientId() {
        return clientId;
    }

    public Status getStatus() {
        return status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public String getUserReview() {
        return userReview;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public int getMasterId() {
        return masterId;
    }

    public int getPrice() {
        return price;
    }

    private boolean isClosed() {
        return this.status.type == Status.Type.CLOSED;
    }

    private static void validateDescription(String description)
            throws InvalidOperationException {
        if (description.length() == 0) {
            logger.fatal("Description cannot be zero-length");
            throw new InvalidOperationException(ExceptionMessages.INVALID_DESCRIPTION);
        }
    }

    private void validateMutability() {
        if (isClosed()) {
            logger.fatal("Request#{} is closed. Unable to modify closed Request", id);
            throw new IllegalStateException();
        }
    }
}
