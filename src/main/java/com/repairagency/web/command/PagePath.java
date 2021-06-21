package com.repairagency.web.command;

/**
 * Page path constants
 */
public class PagePath {

    private PagePath(){
    }

    public static final String GUEST = "/main/role-dependent/guest";
    public static final String CLIENT = "/main/role-dependent/client";
    public static final String MASTER = "/main/role-dependent/master";
    public static final String MANAGER = "/main/role-dependent/manager";
    public static final String ADMIN = "/main/role-dependent/admin";

    public static final String HOME = "/main/home.jsp";
    public static final String ERROR = "/main/error.jsp";
    public static final String LOGIN = "/main/login.jsp";
    public static final String SETTINGS = "/main/role-dependent/profile-settings.jsp";

    public static final String REQUEST_INFO = "/request-info.jsp";
    public static final String USER_INFO = "/user-info.jsp";

    public static final String CLIENT_REQUESTS = "/main/role-dependent/client/client-requests.jsp";
    public static final String CLIENT_PAYMENT_HISTORY = "/main/role-dependent/client/payment-history.jsp";
    public static final String MANAGER_REQUESTS = "/main/role-dependent/manager/manager-requests.jsp";
    public static final String MASTER_REQUESTS = "/main/role-dependent/master/master-requests.jsp";
    public static final String MANAGER_REQUEST_INFO = "/main/role-dependent/manager/request-info.jsp";
    public static final String MASTER_REQUEST_INFO = "/main/role-dependent/master/request-info.jsp";
    public static final String MANAGER_USER_INFO = "/main/role-dependent/manager/user-info.jsp";
    public static final String ADMIN_USERS = "/main/role-dependent/admin/admin-users.jsp";
    public static final String ADMIN_CREATE_USER = "/main/role-dependent/admin/create-user.jsp";
}
