package com.repairagency.database.wrapper;

import com.repairagency.entity.bean.Request;

public class ManagerRequestData {

    private Request request;
    private String clientLogin;
    private String masterLogin;

    public ManagerRequestData(Request request, String clientLogin, String masterLogin) {
        this.request = request;
        this.clientLogin = clientLogin;
        this.masterLogin = masterLogin;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request requestList) {
        this.request = requestList;
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

}
