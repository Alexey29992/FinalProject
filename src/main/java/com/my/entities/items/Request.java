package com.my.entities.items;

import com.my.entities.users.Client;
import com.my.entities.users.Master;
import com.my.exceptions.ExceptionMessages;
import com.my.exceptions.InvalidOperationException;
import com.my.utils.RequestManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Request implements Serializable {

    private static final Logger logger = LogManager.getLogger();

    private int id;
    private final int clientId;
    private final LocalDateTime creationDate;
    private Status status;
    private String description;

    private LocalDateTime completionDate;
    private String userReview;
    private String cancelReason;
    private int masterId;
    private int price;

    public enum Status {
        NEW(Type.OPEN),
        WAITING_FOR_PAYMENT(Type.OPEN),
        PAID(Type.OPEN),
        CANCELLED(Type.CLOSED),
        IN_PROCESS(Type.OPEN),
        DONE(Type.CLOSED);

        public enum Type {
            OPEN,
            CLOSED
        }

        private final Type type;

        Status(Type type) {
            this.type = type;
        }

        public Type getType() {
            return type;
        }
    }

    private Request(String description, Client client) {
        this.description = description;
        this.clientId = client.getId();
        status = Status.NEW;
        creationDate = LocalDateTime.now();
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
            completionDate = LocalDateTime.now();
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCompletionDate() {
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
