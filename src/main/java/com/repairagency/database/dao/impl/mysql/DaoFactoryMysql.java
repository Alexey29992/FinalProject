package com.repairagency.database.dao.impl.mysql;

import com.repairagency.bean.User;
import com.repairagency.bean.data.PaymentRecord;
import com.repairagency.bean.data.Request;
import com.repairagency.database.dao.*;

import java.sql.Connection;

/**
 * MySQL realization of {@link DaoFactory} interface. Represents concrete factory
 * to retrieve MySQL realizations of DAO objects
 */

public class DaoFactoryMysql implements DaoFactory {

    @Override
    public Dao<PaymentRecord> getPaymentRecordDao(Connection connection) {
        return new PaymentRecordDaoMysql(connection);
    }

    @Override
    public Dao<Request> getRequestDao(Connection connection) {
        return new RequestDaoMysql(connection);
    }

    @Override
    public Dao<User> getUserDao(Connection connection) {
        return new UserDaoMysql(connection);
    }

}
