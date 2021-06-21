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

    /**
     * Returns new MySQL implementation of {@link PaymentRecord} {@link Dao}
     * on given {@link Connection}
     * @param connection Connection object on which Dao will be created
     * @return Dao object for PaymentRecord
     * @throws DBException if given Connection object is null
     */
    Dao<PaymentRecord> getPaymentRecordDao(Connection connection) throws DBException;

    /**
     * Returns new MySQL implementation of {@link Request} {@link Dao}
     * on given {@link Connection}
     * @param connection Connection object on which Dao will be created
     * @return Dao object for Request
     * @throws DBException if given Connection object is null
     */
    Dao<Request> getRequestDao(Connection connection) throws DBException;

    /**
     * Returns new MySQL implementation of {@link User} {@link Dao}
     * on given {@link Connection}
     * @param connection Connection object on which Dao will be created
     * @return Dao object for User
     * @throws DBException if given Connection object is null
     */
    Dao<User> getUserDao(Connection connection) throws DBException;

}
