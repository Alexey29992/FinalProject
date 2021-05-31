package com.repairagency.config;

public class Config {

    private Config() {
    }

    public static final int PASSWORD_LENGTH_MIN = 4;
    public static final int PASSWORD_LENGTH_MAX = 20;
    public static final int LOGIN_LENGTH_MIN = 3;
    public static final int LOGIN_LENGTH_MAX = 20;

    public static final int ROWS_PER_PAGE = 20;

    public static final String ADMIN_LOGIN = "admin";
    public static final String ADMIN_PASSWORD = "admin";

}
