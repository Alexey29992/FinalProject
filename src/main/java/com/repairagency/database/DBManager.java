package com.repairagency.database;

import com.repairagency.database.dao.DaoFactory;
import com.repairagency.database.dao.impl.mysql.DaoFactoryMysql;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Utility class that provide methods for initialize {@link DataSource} and {@link DaoFactory},
 * receive {@link Connection}, commit and rollback transaction etc.
 */
public class DBManager {

    private static final Logger logger = LogManager.getLogger();

    private static DaoFactory daoFactory;
    private static DataSource dataSource;

    private DBManager() {
    }

    /**
     * Initializes {@link DataSource} with given on context loading resource data (context.xml).
     * @throws IllegalStateException if initialization of DataSource couldn't be done
     */
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
            String message = "DataSource cannot be initialized";
            logger.fatal(message, ex);
            throw new IllegalStateException(message);
        }
    }

    /**
     * Receives instance of {@link DaoFactory}.
     * @return DaoFactory that was initialized during context creation
     * @throws DBException if DaoFactory was not initialized
     */
    public static DaoFactory getDaoFactory() throws DBException {
        if (daoFactory == null) {
            String message = "DAOFactory is not initialized";
            logger.error(message);
            throw new DBException(message, ErrorMessages.DB_INTERNAL);
        }
        return daoFactory;
    }

    /**
     * Initializes {@link DaoFactory} (by MySQL implementation)
     */
    public static void initDaoFactory() {
        daoFactory = new DaoFactoryMysql();
    }

    /**
     * Receives {@link Connection} object from initialized {@link DataSource}.
     * @return new Connection object from the pool
     * @throws DBException if DataSource had not been initialized before this method was called
     */
    public static Connection getConnection() throws DBException {
        if (dataSource == null) {
            String message = "DataSource is not initialized";
            logger.error(message);
            throw new DBException(message, ErrorMessages.DB_INTERNAL);
        }
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            return conn;
        } catch (SQLException ex) {
            String message = "Cannot get Connection";
            logger.error(message, ex);
            closeConnection(conn);
            throw new DBException(message, ErrorMessages.DB_INTERNAL);
        }
    }

    /**
     * Commits current transaction by calling {@link Connection#commit()}
     * on given {@link Connection} object.
     * @param connection Connection object to commit current transaction
     * @throws DBException if current transaction can't be committed
     */
    public static void commitTransaction(Connection connection) throws DBException {
        try {
            if (connection != null) {
                connection.commit();
            }
        } catch (SQLException ex) {
            String message = "Cannot commit Transaction";
            logger.error(message, ex);
            throw new DBException(message, ErrorMessages.DB_INTERNAL);
        }
    }

    /**
     * Rollbacks current transaction by calling {@link Connection#rollback()}
     * on given {@link Connection} object. If any SQL exceptions occurred during
     * rollback they will be just logged and ignored.
     * @param connection Connection object to rollback current transaction
     */
    public static void rollbackTransaction(Connection connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException ex) {
            logger.error("Cannot rollback Transaction", ex);
        }
    }

    /**
     * Closes given {@link Connection} (returns it back to connection pool of DataSource).
     * If any SQL exceptions occurred during closing they will be just logged and ignored.
     * @param connection Connection object to be released
     */
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
