package com.repairagency.database;

/**
 * Field names in the database
 */
public class DBFields {

    private DBFields() {
    }

    public static final String TABLE_USERS = "user";
    public static final String TABLE_REQUESTS = "request";
    public static final String TABLE_CLIENTS = "client";
    public static final String TABLE_MASTERS = "master";
    public static final String TABLE_PAYMENT_RECORDS = "payment_record";
    public static final String TABLE_STATUSES = "status";
    public static final String TABLE_ROLES = "role";

    public static final String USER_LOGIN = "login";
    public static final String USER_PASSWORD = "password";
    public static final String USER_ROLE_ID = "role_id";

    public static final String CLIENT_PHONE = "ph_number";
    public static final String CLIENT_BALANCE = "balance";

    public static final String REQUEST_CREATION_DATE = "creation_date";
    public static final String REQUEST_DESCRIPTION = "description";
    public static final String REQUEST_COMPLETION_DATE = "completion_date";
    public static final String REQUEST_USER_REVIEW = "user_review";
    public static final String REQUEST_CANCEL_REASON = "cancel_reason";
    public static final String REQUEST_CLIENT_ID = "client_id";
    public static final String REQUEST_STATUS_ID = "status_id";
    public static final String REQUEST_MASTER_ID = "master_id";
    public static final String REQUEST_PRICE = "price";

    public static final String PR_DATE = "date";
    public static final String PR_DESTINATION = "destination";
    public static final String PR_SUM = "sum";
    public static final String PR_CLIENT_ID = "client_id";

    public static final String ID = "id";
    public static final String STATUS_NAME = "status_name";
    public static final String ROLE_NAME = "role_name";

}
