package com.repairagency.database.dao.mysql;

import com.repairagency.database.DbFieldNames;
import com.repairagency.database.dao.AbstractDao;
import com.repairagency.database.DBManager;
import com.repairagency.database.dao.RequestDao;
import com.repairagency.entities.items.Request;
import com.repairagency.exceptions.DBException;
import com.repairagency.exceptions.ExceptionMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequestDaoMysql extends AbstractDao implements RequestDao {

    private static final Logger logger = LogManager.getLogger();

    public RequestDaoMysql(Connection connection) {
        super(connection);
    }

    @Override
    public int addEntity(Request req) throws DBException {
        int statusId = getStatusId(req.getStatus());
        return addEntity(req, statusId);
    }

    @Override
    public void updateEntity(Request req) throws DBException {
        int statusId = getStatusId(req.getStatus());
        updateEntity(req, statusId);
    }

    private static final String QUERY_DELETE = String.format("DELETE FROM %s WHERE %s = ?",
            DbFieldNames.TABLE_REQUESTS,
            DbFieldNames.ID);

    @Override
    public void removeEntity(Request req) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            statement.setInt(1, req.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("Request cannot be deleted", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        }
    }

    private static final String QUERY_GET_BY_ID = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s JOIN %2$s ON %4$s = %2$s.%5$s WHERE %5$s = ?",
            DbFieldNames.TABLE_REQUESTS,
            DbFieldNames.TABLE_STATUSES,
            DbFieldNames.STATUS_NAME,
            DbFieldNames.REQUEST_STATUS_ID,
            DbFieldNames.ID);

    @Override
    public Request getEntityById(int id) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_ID)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("ResultSet is empty. Cannot get Request");
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_UNEXPECTED);
            }
            return getRequestInstance(resultSet);
        } catch (SQLException ex) {
            logger.error("Cannot get Request with given id", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_ALL = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s JOIN %2$s ON %4$s = %2$s.%5$s" +
                    " ORDER BY ? LIMIT ?, ?",
            DbFieldNames.TABLE_REQUESTS,
            DbFieldNames.TABLE_STATUSES,
            DbFieldNames.STATUS_NAME,
            DbFieldNames.REQUEST_STATUS_ID,
            DbFieldNames.ID);

    @Override
    public List<Request> getEntityListAll(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        List<Request> requests = new ArrayList<>();
        ResultSet resultSet = null;
        int startPos = chunkNumber * chunkSize;
        int endPos = startPos + chunkSize;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_ALL)) {
            int k = 0;
            statement.setString(++k, sortingFactor);
            statement.setInt(++k, startPos);
            statement.setInt(++k, endPos);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                requests.add(getRequestInstance(resultSet));
            }
            return requests;
        } catch (SQLException ex) {
            logger.error("Cannot get Request list", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_BY_MASTER = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s JOIN %2$s ON %4$s = %2$s.%5$s" +
                    " WHERE %6$s = ? ORDER BY ? LIMIT ?, ?",
            DbFieldNames.TABLE_REQUESTS,
            DbFieldNames.TABLE_STATUSES,
            DbFieldNames.STATUS_NAME,
            DbFieldNames.REQUEST_STATUS_ID,
            DbFieldNames.ID,
            DbFieldNames.REQUEST_MASTER_ID);

    @Override
    public List<Request> getEntityListByMaster(int masterId, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        List<Request> requests = new ArrayList<>();
        ResultSet resultSet = null;
        int startPos = chunkNumber * chunkSize;
        int endPos = startPos + chunkSize;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_MASTER)) {
            int k = 0;
            statement.setInt(++k, masterId);
            statement.setString(++k, sortingFactor);
            statement.setInt(++k, startPos);
            statement.setInt(++k, endPos);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                requests.add(getRequestInstance(resultSet));
            }
            return requests;
        } catch (SQLException ex) {
            logger.error("Cannot get Request list for Master", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_BY_CLIENT = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s" +
                    " JOIN %2$s ON %4$s = %2$s.%5$s" +
                    " WHERE %6$s = ? ORDER BY ? LIMIT ?, ?",
            DbFieldNames.TABLE_REQUESTS,
            DbFieldNames.TABLE_STATUSES,
            DbFieldNames.STATUS_NAME,
            DbFieldNames.REQUEST_STATUS_ID,
            DbFieldNames.ID,
            DbFieldNames.REQUEST_CLIENT_ID);

    @Override
    public List<Request> getEntityListByClient(int clientId, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        List<Request> requests = new ArrayList<>();
        ResultSet resultSet = null;
        int startPos = chunkNumber * chunkSize;
        int endPos = startPos + chunkSize;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_CLIENT)) {
            int k = 0;
            statement.setInt(++k, clientId);
            statement.setString(++k, sortingFactor);
            statement.setInt(++k, startPos);
            statement.setInt(++k, endPos);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                requests.add(getRequestInstance(resultSet));
            }
            return requests;
        } catch (SQLException ex) {
            logger.error("Cannot get Request list for Client", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_BY_STATUS = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s" +
                    " JOIN %2$s ON %4$s = %2$s.%5$s" +
                    " WHERE %2$s.%3$s = ? ORDER BY ? LIMIT ?, ?",
            DbFieldNames.TABLE_REQUESTS,
            DbFieldNames.TABLE_STATUSES,
            DbFieldNames.STATUS_NAME,
            DbFieldNames.REQUEST_STATUS_ID,
            DbFieldNames.ID);

    @Override
    public List<Request> getEntityListByStatus(Request.Status status, int chunkSize,
                                               int chunkNumber, String sortingFactor) throws DBException {
        List<Request> requests = new ArrayList<>();
        ResultSet resultSet = null;
        int startPos = chunkNumber * chunkSize;
        int endPos = startPos + chunkSize;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_STATUS)) {
            int k = 0;
            statement.setString(++k, status.name());
            statement.setString(++k, sortingFactor);
            statement.setInt(++k, startPos);
            statement.setInt(++k, endPos);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                requests.add(getRequestInstance(resultSet));
            }
            return requests;
        } catch (SQLException ex) {
            logger.error("Cannot get Request list for Client", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_STATUS_ID = String.format("SELECT %s FROM %s WHERE %s = ?",
            DbFieldNames.ID,
            DbFieldNames.TABLE_STATUSES,
            DbFieldNames.STATUS_NAME);

    private int getStatusId(Request.Status status) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_STATUS_ID)) {
            statement.setString(1, status.name());
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("ResultSet is empty. Cannot get id of queried Status");
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_UNEXPECTED);
            }
            return resultSet.getInt(DbFieldNames.ID);
        } catch (SQLException ex) {
            logger.error("Cannot get id of the queried Status", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private Request getRequestInstance(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(DbFieldNames.ID);
        int clientId = resultSet.getInt(DbFieldNames.REQUEST_CLIENT_ID);
        LocalDateTime creationDate = resultSet.getTimestamp(DbFieldNames.REQUEST_CREATION_DATE).toLocalDateTime();
        Request.Status status = Request.Status.valueOf(resultSet.getString(DbFieldNames.STATUS_NAME));
        String description = resultSet.getString(DbFieldNames.REQUEST_DESCRIPTION);
        LocalDateTime completionDate = resultSet.getTimestamp(DbFieldNames.REQUEST_COMPLETION_DATE).toLocalDateTime();
        String userReview = resultSet.getString(DbFieldNames.REQUEST_USER_REVIEW);
        String cancelReason = resultSet.getString(DbFieldNames.REQUEST_CANCEL_REASON);
        int masterId = resultSet.getInt(DbFieldNames.REQUEST_MASTER_ID);
        int price = resultSet.getInt(DbFieldNames.REQUEST_PRICE);
        return new Request(id, clientId, creationDate, status, description,
                completionDate, userReview, cancelReason, masterId, price);
    }

    private static final String QUERY_ADD = String.format(
            "INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
            DbFieldNames.TABLE_REQUESTS,
            DbFieldNames.REQUEST_CLIENT_ID,
            DbFieldNames.REQUEST_CREATION_DATE,
            DbFieldNames.REQUEST_STATUS_ID,
            DbFieldNames.REQUEST_DESCRIPTION
    );

    private int addEntity(Request req, int statusId) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_ADD, Statement.RETURN_GENERATED_KEYS)) {
            int k = 0;
            statement.setInt(++k, req.getClientId());
            statement.setTimestamp(++k, Timestamp.valueOf(req.getCreationDate()));
            statement.setInt(++k, statusId);
            statement.setString(++k, req.getDescription());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (!resultSet.next()) {
                logger.error("ResultSet is empty. Cannot get Request id");
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_UNEXPECTED);
            }
            return resultSet.getInt(1);
        } catch (SQLException ex) {
            logger.error("Request cannot be added to database", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_UNEXPECTED, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_UPDATE = String.format(
            "UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, " +
                    "%s = ? %s = ?, %s = ?, %s = ?, %s = ?, WHERE %s = ?",
            DbFieldNames.TABLE_REQUESTS,
            DbFieldNames.REQUEST_CLIENT_ID,
            DbFieldNames.REQUEST_CREATION_DATE,
            DbFieldNames.REQUEST_STATUS_ID,
            DbFieldNames.REQUEST_DESCRIPTION,
            DbFieldNames.REQUEST_COMPLETION_DATE,
            DbFieldNames.REQUEST_USER_REVIEW,
            DbFieldNames.REQUEST_CANCEL_REASON,
            DbFieldNames.REQUEST_MASTER_ID,
            DbFieldNames.REQUEST_PRICE,
            DbFieldNames.ID);

    private void updateEntity(Request req, int statusId) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            int k = 0;
            statement.setInt(++k, req.getClientId());
            statement.setTimestamp(++k, Timestamp.valueOf(req.getCreationDate()));
            statement.setInt(++k, statusId);
            statement.setString(++k, req.getDescription());
            statement.setTimestamp(++k, Timestamp.valueOf(req.getCompletionDate()));
            statement.setString(++k, req.getUserReview());
            statement.setString(++k, req.getCancelReason());
            statement.setInt(++k, req.getMasterId());
            statement.setInt(++k, req.getPrice());
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("Request cannot be updated", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        }
    }

}
