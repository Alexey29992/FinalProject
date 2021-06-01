package com.repairagency.database;

import com.repairagency.database.dao.DaoFactory;
import com.repairagency.database.dao.mysql.DaoFactoryMysql;
import com.repairagency.exceptions.DBException;
import com.repairagency.exceptions.ExceptionMessages;
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

    public static void initDataSource() {
        logger.debug("Initializing DataSource");
        try {
            Context initContext = new InitialContext();
            logger.trace("init context : {}", initContext);
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            logger.trace("env context : {}", envContext);
            dataSource = (DataSource) envContext.lookup("jdbc/radb");
            logger.trace("data source : {}", dataSource);
        } catch (NamingException ex) {
            logger.fatal("DataSource cannot be initialized", ex);
            throw new IllegalStateException(ExceptionMessages.DB_INTERNAL);
        }
//        try (Connection conn = dataSource.getConnection()) {
//            logger.trace("connection : {}", conn);
//        } catch (SQLException ex) {
//            logger.fatal("Unable to get connection from DataSource", ex);
//            throw new IllegalStateException(ExceptionMessages.DB_NO_CONNECTION);
//        }
    }

    public static DaoFactory getDaoFactory() throws DBException {
        if (daoFactory == null) {
            logger.error("DAO factory is null");
            throw new DBException(ExceptionMessages.DB_NO_FACTORY);
        }
        return daoFactory;
    }

    public static void setDaoFactory(DaoFactory factory) {
        daoFactory = factory;
    }
    public static void defaultDaoFactory() {
        daoFactory = new DaoFactoryMysql();
    }

    public static Connection getConnection() throws DBException {
        if (dataSource == null) {
            throw new DBException(ExceptionMessages.DB_NO_CONNECTION);
        }
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            return conn;
        } catch (SQLException ex) {
            logger.error("Cannot get Connection", ex);
            closeConnection(conn);
            throw new DBException(ExceptionMessages.DB_NO_CONNECTION);
        }
    }

    public static void commitTransaction(Connection connection) throws DBException {
        try {
            if (connection != null) {
                connection.commit();
            }
        } catch (SQLException ex) {
            logger.error("Cannot commit Transaction", ex);
            throw new DBException(ExceptionMessages.DB_INTERNAL);
        }
    }

    public static void rollbackTransaction(Connection connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException ex) {
            logger.error("Cannot rollback Transaction", ex);
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            logger.error("Cannot close Connection", ex);
        }
    }

}
