package com.repairagency.bean.data;

import com.repairagency.bean.AbstractBean;

import java.time.LocalDateTime;

/**
 * Represents Client's repair request
 */

public class Request extends AbstractBean {

    private int clientId;
    private int masterId;
    private String clientLogin;
    private String masterLogin;
    private int price;
    private LocalDateTime creationDate;
    private LocalDateTime completionDate;
    private Status status;
    private String description;
    private String userReview;
    private String cancelReason;

    public enum Status {
        NEW(),
        WAIT_FOR_PAYMENT(),
        PAID(),
        CANCELLED(),
        IN_PROCESS(),
        DONE();
        public String toLowerCaseString() {
            return super.toString().toLowerCase();
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

    public String getClientLogin() {
        return clientLogin;
    }

    public void setClientLogin(String clientLogin) {
        this.clientLogin = clientLogin;
    }

    public String getMasterLogin() {
        return masterLogin;
    }

    public void setMasterLogin(String masterLogin) {
        this.masterLogin = masterLogin;
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
                ", clientLogin" + clientLogin +
                ", masterLogin" + masterLogin +
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
