package com.repairagency.entity.bean;

import com.repairagency.entity.PersistentEntity;

import java.time.LocalDateTime;

public class Request extends PersistentEntity {

    private int clientId;
    private int masterId;
    private int price;
    private LocalDateTime creationDate;
    private LocalDateTime completionDate;
    private Status status;
    private String description;
    private String userReview;
    private String cancelReason;

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

    public Request() {
    }

    public Request(String description, int clientId) {
        this.description = description;
        this.clientId = clientId;
        status = Status.NEW;
        creationDate = LocalDateTime.now();
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserReview() {
        return userReview;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    @Override
    public String toString() {
        return "Request{" +
                super.toString() +
                ", clientId=" + clientId +
                ", masterId=" + masterId +
                ", price=" + price +
                ", creationDate=" + creationDate +
                ", completionDate=" + completionDate +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", userReview='" + userReview + '\'' +
                ", cancelReason='" + cancelReason + '\'' +
                '}';
    }

}
