package com.repairagency.entities.items;

import com.repairagency.entities.Item;
import com.repairagency.exceptions.InvalidOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class Request extends Item {

    private static final Logger logger = LogManager.getLogger();

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
        WAIT_FOR_PAYMENT(Type.OPEN),
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

    public Request(String description, int clientId) {
        this.description = description;
        this.clientId = clientId;
        status = Status.NEW;
        creationDate = LocalDateTime.now();
    }

    public Request(int id, int clientId, LocalDateTime creationDate, Status status,
                   String description, LocalDateTime completionDate, String userReview,
                   String cancelReason, int masterId, int price) {
        this.id = id;
        this.clientId = clientId;
        this.creationDate = creationDate;
        this.status = status;
        this.description = description;
        this.completionDate = completionDate;
        this.userReview = userReview;
        this.cancelReason = cancelReason;
        this.masterId = masterId;
        this.price = price;
    }

    public void setStatus(Status status) throws InvalidOperationException {
        validateMutability();
        this.status = status;
        if (isClosed()) {
            completionDate = LocalDateTime.now();
        }
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public void setDescription(String description)
            throws InvalidOperationException {
        validateMutability();
        this.description = description;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }

    public void setMaster(int masterId)
            throws InvalidOperationException {
        validateMutability();
        this.masterId = masterId;
    }

    public void setPrice(int price) {
        this.price = price;
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

    private void validateMutability() throws InvalidOperationException {
        if (isClosed()) {
            logger.error("Request#{} is closed. Unable to modify closed Request", id);
            throw new InvalidOperationException("Request is closed");
        }
    }

    private boolean isClosed() {
        return this.status.type == Status.Type.CLOSED;
    }

}
