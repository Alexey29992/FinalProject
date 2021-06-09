package com.repairagency.database.dao;

import com.repairagency.exception.DBException;

import java.sql.Connection;

public interface DaoFactory {

    PaymentRecordDao getPaymentRecordDao(Connection connection) throws DBException;

    RequestDao getRequestDao(Connection connection) throws DBException;

    UserDao getUserDao(Connection connection) throws DBException;

}
