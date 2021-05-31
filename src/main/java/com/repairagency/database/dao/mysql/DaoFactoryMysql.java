package com.repairagency.database.dao.mysql;

import com.repairagency.database.dao.*;

import java.sql.Connection;

public class DaoFactoryMysql implements DaoFactory {

    @Override
    public PaymentRecordDao getPaymentRecordDao(Connection connection) {
        return new PaymentRecordDaoMysql(connection);
    }

    @Override
    public RequestDao getRequestDao(Connection connection) {
        return new RequestDaoMysql(connection);
    }

    @Override
    public UserDao getUserDao(Connection connection) {
        return new UserDaoMysql(connection);
    }

    @Override
    public WalletDao getWalletDao(Connection connection) {
        return new WalletDaoMysql(connection);
    }

}
