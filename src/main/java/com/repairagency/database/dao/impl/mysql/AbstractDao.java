package com.repairagency.database.dao.impl.mysql;

import com.repairagency.bean.AbstractBean;
import com.repairagency.database.DBManager;
import com.repairagency.database.dao.Dao;
import com.repairagency.database.QueryGetData;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is base for all MySQL realizations of DAO.
 * It holds the {@link Connection} instance and some common for all the DAO methods.
 * @param <T> type of a bean that DAO intended to work with
 */
public abstract class AbstractDao<T extends AbstractBean> implements Dao<T> {

    private static final Logger logger = LogManager.getLogger();

    protected final Connection connection;

    /**
     * Creates Dao object on given {@link Connection} that will be used to process queries
     * until this Dao object exists.
     * @param connection a {@link Connection} to MySQL database
     */
    protected AbstractDao(Connection connection) throws DBException {
        if (connection == null) {
            String message = "Connection is empty";
            logger.error("Connection is empty");
            throw new DBException(message, ErrorMessages.DB_INTERNAL);
        }
        this.connection = connection;
    }

    /**
     * Common method for removing entities from the database. ID column is used to
     * identify removing rows. Inherits should call this method with the
     * appropriate query string and entity ID.
     * @param id ID of entity to be removed
     * @param query SQL query string that will be executed
     * @throws DBException if internal database errors occur
     */
    public void removeEntity(int id, String query) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            String message = "Entity cannot be deleted";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        }
    }

    /**
     * Common method for receive the {@link List} of entities according to given
     * queryData parameter and base query that is defined by {@link #getQueryBase()} in
     * the inheritor class. This method uses {@link QueryGetGenerator} to get the final
     * SQL query from base query and queryData parameter. Entities are created with
     * {@link #getInstance(ResultSet)}
     * @param queryData QueryGetData instance that contains information to get desired
     *                  entity list from the database
     * @return ArrayList of the entities according to queryData.
     * @throws DBException if given queryData not correct or internal database errors occur
     */
    @Override
    public List<T> getEntityList(QueryGetData queryData) throws DBException {
        logger.debug("Fetching entity list");
        String queryBase = getQueryBase();
        String query = new QueryGetGenerator(queryBase, queryData).generateQuery();
        logger.trace("query : {}", query);
        List<T> list = new ArrayList<>();
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(getInstance(resultSet));
            }
            return list;
        } catch (SQLException ex) {
            String message = "Cannot get Entity List";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    /**
     * Closes given ResultSet. If some SQL exceptions are thrown during closing ResultSet
     * they will be only logged and ignored.
     * @param resultSet ResultSet that being closed
     */
    protected void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException ex) {
            logger.error("Cannot close the ResultSet", ex);
        }
    }

    /**
     * Creates single instance of entity from given {@link ResultSet}.
     * This method doesn't invoke {@link ResultSet#next()}.
     * Cursor of ResultSet should be on valid position before calling this method.
     * @param resultSet ResultSet that holds necessary data to create instance
     * @return instance of {@link T} entity
     * @throws SQLException if internal database errors occur
     */
    protected abstract T getInstance(ResultSet resultSet) throws SQLException;

    /**
     * Receives Dao specific SQL SELECT query that retrieves all data from the according table
     * (without any filtering, ordering and limiting). It is used by {@link #getEntityList(QueryGetData)}
     * to create final query with {@link QueryGetGenerator}
     * @return SQL SELECT query that contains information about tables, columns, joins etc.
     */
    protected abstract String getQueryBase();

}
