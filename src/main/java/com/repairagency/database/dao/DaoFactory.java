package com.repairagency.database.dao;

import com.repairagency.bean.User;
import com.repairagency.bean.data.PaymentRecord;
import com.repairagency.bean.data.Request;
import com.repairagency.exception.DBException;

import java.sql.Connection;

/**
 * DaoFactory is an abstract factory that is used to get DAO objects for an appropriate bean.
 * Realizations of this interface are intended for receiving concrete database-dependent factories
 */

public interface DaoFactory {

    Dao<PaymentRecord> getPaymentRecordDao(Connection connection) throws DBException;

    Dao<Request> getRequestDao(Connection connection) throws DBException;

    Dao<User> getUserDao(Connection connection) throws DBException;

}
