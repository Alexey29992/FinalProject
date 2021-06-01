package com.repairagency.database.dao.mysql;

import com.repairagency.database.DbNames;
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
            DbNames.TABLE_REQUESTS,
            DbNames.ID);

    @Override
    public void removeEntity(Request req) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            statement.setInt(1, req.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            String message = "Request#" + req.getId() + " cannot be deleted";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        }
    }

    private static final String QUERY_GET_BY_ID = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s JOIN %2$s ON %4$s = %2$s.%5$s WHERE %5$s = ?",
            DbNames.TABLE_REQUESTS,
            DbNames.TABLE_STATUSES,
            DbNames.STATUS_NAME,
            DbNames.REQUEST_STATUS_ID,
            DbNames.ID);

    @Override
    public Request getEntityById(int id) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_ID)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("Cannot get Request#{}", id);
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_EMPTY_RESULT_SET,
                        ExceptionMessages.DB_NOT_FOUND);
            }
            return getRequestInstance(resultSet);
        } catch (SQLException ex) {
            String message = "Cannot get Request#" + id;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_ALL = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s JOIN %2$s ON %4$s = %2$s.%5$s" +
                    " ORDER BY ? LIMIT ?, ?",
            DbNames.TABLE_REQUESTS,
            DbNames.TABLE_STATUSES,
            DbNames.STATUS_NAME,
            DbNames.REQUEST_STATUS_ID,
            DbNames.ID);

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
            String message = "Cannot get Request List";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_BY_MASTER = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s JOIN %2$s ON %4$s = %2$s.%5$s" +
                    " WHERE %6$s = ? ORDER BY ? LIMIT ?, ?",
            DbNames.TABLE_REQUESTS,
            DbNames.TABLE_STATUSES,
            DbNames.STATUS_NAME,
            DbNames.REQUEST_STATUS_ID,
            DbNames.ID,
            DbNames.REQUEST_MASTER_ID);

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
            String message = "Cannot get Request List for Master#" + masterId;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_BY_CLIENT = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s" +
                    " JOIN %2$s ON %4$s = %2$s.%5$s" +
                    " WHERE %6$s = ? ORDER BY ? LIMIT ?, ?",
            DbNames.TABLE_REQUESTS,
            DbNames.TABLE_STATUSES,
            DbNames.STATUS_NAME,
            DbNames.REQUEST_STATUS_ID,
            DbNames.ID,
            DbNames.REQUEST_CLIENT_ID);

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
            String message = "Cannot get Request List for Client#" + clientId;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_BY_STATUS = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s" +
                    " JOIN %2$s ON %4$s = %2$s.%5$s" +
                    " WHERE %2$s.%3$s = ? ORDER BY ? LIMIT ?, ?",
            DbNames.TABLE_REQUESTS,
            DbNames.TABLE_STATUSES,
            DbNames.STATUS_NAME,
            DbNames.REQUEST_STATUS_ID,
            DbNames.ID);

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
            String message = "Cannot get Request List with Status#" + status;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_STATUS_ID = String.format("SELECT %s FROM %s WHERE %s = ?",
            DbNames.ID,
            DbNames.TABLE_STATUSES,
            DbNames.STATUS_NAME);

    private int getStatusId(Request.Status status) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_STATUS_ID)) {
            statement.setString(1, status.name());
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("Cannot get id of Status {}", status);
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_EMPTY_RESULT_SET,
                        ExceptionMessages.DB_NOT_FOUND);
            }
            return resultSet.getInt(DbNames.ID);
        } catch (SQLException ex) {
            String message = "Cannot get id of Status " + status;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private Request getRequestInstance(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(DbNames.ID);
        int clientId = resultSet.getInt(DbNames.REQUEST_CLIENT_ID);
        LocalDateTime creationDate = resultSet.getTimestamp(DbNames.REQUEST_CREATION_DATE).toLocalDateTime();
        Request.Status status = Request.Status.valueOf(resultSet.getString(DbNames.STATUS_NAME));
        String description = resultSet.getString(DbNames.REQUEST_DESCRIPTION);
        LocalDateTime completionDate = resultSet.getTimestamp(DbNames.REQUEST_COMPLETION_DATE).toLocalDateTime();
        String userReview = resultSet.getString(DbNames.REQUEST_USER_REVIEW);
        String cancelReason = resultSet.getString(DbNames.REQUEST_CANCEL_REASON);
        int masterId = resultSet.getInt(DbNames.REQUEST_MASTER_ID);
        int price = resultSet.getInt(DbNames.REQUEST_PRICE);
        return new Request(id, clientId, creationDate, status, description,
                completionDate, userReview, cancelReason, masterId, price);
    }

    private static final String QUERY_ADD = String.format(
            "INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
            DbNames.TABLE_REQUESTS,
            DbNames.REQUEST_CLIENT_ID,
            DbNames.REQUEST_CREATION_DATE,
            DbNames.REQUEST_STATUS_ID,
            DbNames.REQUEST_DESCRIPTION
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
                logger.error("Cannot receive Request id");
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_NO_KEY, ExceptionMessages.DB_INTERNAL);
            }
            return resultSet.getInt(1);
        } catch (SQLException ex) {
            String message = "Request cannot be added to database";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_UPDATE = String.format(
            "UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, " +
                    "%s = ? %s = ?, %s = ?, %s = ?, %s = ?, WHERE %s = ?",
            DbNames.TABLE_REQUESTS,
            DbNames.REQUEST_CLIENT_ID,
            DbNames.REQUEST_CREATION_DATE,
            DbNames.REQUEST_STATUS_ID,
            DbNames.REQUEST_DESCRIPTION,
            DbNames.REQUEST_COMPLETION_DATE,
            DbNames.REQUEST_USER_REVIEW,
            DbNames.REQUEST_CANCEL_REASON,
            DbNames.REQUEST_MASTER_ID,
            DbNames.REQUEST_PRICE,
            DbNames.ID);

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
            String message = "Request#" + req.getId() + " cannot be updated";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_NOT_FOUND, ex);
        }
    }

}
