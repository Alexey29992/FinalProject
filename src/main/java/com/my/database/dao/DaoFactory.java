package com.my.database.dao;

import com.my.exceptions.DBException;

import java.sql.Connection;

public interface DaoFactory {

    PaymentRecordDao getPaymentRecordDao(Connection connection) throws DBException;

    RequestDao getRequestDao(Connection connection) throws DBException;

    UserDao getUserDao(Connection connection) throws DBException;

    WalletDao getWalletDao(Connection connection) throws DBException;

}
