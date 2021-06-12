package com.repairagency.database.dao;

import com.repairagency.bean.User;
import com.repairagency.bean.data.PaymentRecord;
import com.repairagency.bean.data.Request;
import com.repairagency.exception.DBException;

import java.sql.Connection;

public interface DaoFactory {

    Dao<PaymentRecord> getPaymentRecordDao(Connection connection) throws DBException;

    Dao<Request> getRequestDao(Connection connection) throws DBException;

    Dao<User> getUserDao(Connection connection) throws DBException;

}
