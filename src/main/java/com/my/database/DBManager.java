package com.my.database;

import com.my.database.dao.DaoFactory;
import com.my.database.dao.mysql.DaoFactoryMysql;
import com.my.exceptions.DBException;
import com.my.exceptions.ExceptionMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBManager {

    private static final Logger logger = LogManager.getLogger();
    private static DaoFactory daoFactory;

    private static DataSource dataSource;

    private DBManager() {
    }

    public static void init() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/myDB");
        } catch (NamingException ex) {
            logger.error("DataSource cannot be initialized");
            throw new IllegalStateException(ExceptionMessages.DB_FATAL);
        }
    }

    public static DaoFactory getDaoFactory() {
        return daoFactory;
    }

    public static void setDaoFactory(DaoFactory factory) {
        daoFactory = factory;
    }

    public static Connection getConnection() throws DBException {
        if (dataSource == null) {
            throw new DBException(ExceptionMessages.DB_NO_CONNECTION);
        }
        try {
            Connection conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            return conn;
        } catch (SQLException ex) {
            logger.error("Cannot get Connection", ex);
            throw new DBException(ExceptionMessages.DB_NO_CONNECTION);
        }
    }

    public static void commit(Connection connection) throws DBException {
        try {
            connection.commit();
        } catch (SQLException ex) {
            logger.error("Cannot commit Transaction", ex);
            throw new DBException(ExceptionMessages.DB_UNEXPECTED);
        }
    }

    public static void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            logger.error("Cannot rollback Transaction", ex);
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException ex) {
            logger.error("Cannot close Connection", ex);
        }
    }

}
