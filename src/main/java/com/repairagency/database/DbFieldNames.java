package com.repairagency.database;

public class DbFieldNames {

    private DbFieldNames() {
    }

    public static final String TABLE_USERS = "user";
    public static final String TABLE_REQUESTS = "request";
    public static final String TABLE_WALLETS = "wallet";
    public static final String TABLE_CLIENTS = "client";
    public static final String TABLE_MASTERS = "master";
    public static final String TABLE_PAYMENT_RECORDS = "payment_record";
    public static final String TABLE_STATUSES = "status";
    public static final String TABLE_ROLES = "role";

    public static final String ID = "id";

    public static final String USERS_LOGIN = "login";
    public static final String USERS_PASSWORD = "password";
    public static final String USERS_ROLE_ID = "role";

    public static final String REQUEST_CREATION_DATE = "creation_date";
    public static final String REQUEST_STATUS_ID = "status_id";
    public static final String REQUEST_DESCRIPTION = "description";
    public static final String REQUEST_COMPLETION_DATE = "completion_date";
    public static final String REQUEST_USER_REVIEW = "user_review";
    public static final String REQUEST_CANCEL_REASON = "cancel_reason";
    public static final String REQUEST_MASTER_ID = "master_id";
    public static final String REQUEST_PRICE = "price";
    public static final String REQUEST_CLIENT_ID = "client_id";

    public static final String WALLET_BALANCE = "balance";
    public static final String WALLET_USER_ID = "client_id";

    public static final String PAYMENT_RECORD_DATE = "date";
    public static final String PAYMENT_RECORD_SUM = "sum";
    public static final String PAYMENT_RECORD_DESTINATION = "destination";
    public static final String PAYMENT_RECORD_WALLET_ID = "wallet_id";

    public static final String STATUS_NAME = "status_name";

    public static final String ROLE_NAME = "role_name";

}
