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
 *
 * @param <T> type of bean that DAO intended to work with
 */

public abstract class AbstractDao<T extends AbstractBean> implements Dao<T> {

    private static final Logger logger = LogManager.getLogger();

    protected final Connection connection;

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

    protected AbstractDao(Connection connection) {
        this.connection = connection;
    }

    protected void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException ex) {
            logger.error("Cannot close the ResultSet", ex);
        }
    }

    protected abstract T getInstance(ResultSet resultSet) throws SQLException;

    protected abstract String getQueryBase();

}
