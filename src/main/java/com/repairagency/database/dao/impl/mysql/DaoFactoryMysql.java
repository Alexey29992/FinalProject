package com.repairagency.database.dao.impl.mysql;

import com.repairagency.bean.User;
import com.repairagency.bean.data.PaymentRecord;
import com.repairagency.bean.data.Request;
import com.repairagency.database.dao.*;
import com.repairagency.exception.DBException;

import java.sql.Connection;

/**
 * MySQL realization of {@link DaoFactory} interface. Represents concrete factory
 * to retrieve MySQL realizations of DAO objects
 */
public class DaoFactoryMysql implements DaoFactory {

    /**
     * Returns new MySQL implementation of {@link PaymentRecord} {@link Dao}
     * on given {@link Connection}
     * @param connection Connection object on which Dao will be created
     * @return Dao object for PaymentRecord
     * @throws DBException if given Connection object is null
     */
    @Override
    public Dao<PaymentRecord> getPaymentRecordDao(Connection connection)
            throws DBException {
        return new PaymentRecordDaoMysql(connection);
    }

    /**
     * Returns new MySQL implementation of {@link Request} {@link Dao}
     * on given {@link Connection}
     * @param connection Connection object on which Dao will be created
     * @return Dao object for Request
     * @throws DBException if given Connection object is null
     */
    @Override
    public Dao<Request> getRequestDao(Connection connection)
            throws DBException {
        return new RequestDaoMysql(connection);
    }

    /**
     * Returns new MySQL implementation of {@link User} {@link Dao}
     * on given {@link Connection}
     * @param connection Connection object on which Dao will be created
     * @return Dao object for User
     * @throws DBException if given Connection object is null
     */
    @Override
    public Dao<User> getUserDao(Connection connection)
            throws DBException {
        return new UserDaoMysql(connection);
    }

}
