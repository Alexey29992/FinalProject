package com.repairagency.database.wrapper;

import com.repairagency.entity.bean.Request;

public class RequestData {

    private final Request request;
    private final String clientLogin;
    private final String masterLogin;

    public RequestData(Request request, String clientLogin, String masterLogin) {
        this.request = request;
        this.clientLogin = clientLogin;
        this.masterLogin = masterLogin;
    }

    public Request getRequest() {
        return request;
    }

    public String getClientLogin() {
        return clientLogin;
    }

    public String getMasterLogin() {
        return masterLogin;
    }

}
