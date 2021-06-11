package com.repairagency.database.dao.mysql;

import com.repairagency.database.dao.AbstractDao;
import com.repairagency.database.DBManager;
import com.repairagency.database.dao.RequestDao;
import com.repairagency.database.wrapper.RequestData;
import com.repairagency.entity.bean.Request;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequestDaoMysql extends AbstractDao implements RequestDao {

    private static final Logger logger = LogManager.getLogger();

    private static final String QUERY_DELETE = "DELETE FROM request WHERE id = ?";
    private static final String QUERY_UPDATE = "UPDATE request SET client_id = ?, " +
            "creation_date = ?, status_id = ?, description = ?, completion_date = ?, " +
            "user_review = ?, cancel_reason = ?, master_id = ?, price = ? WHERE id = ?";
    private static final String QUERY_ADD = "INSERT INTO request (client_id, creation_date," +
            " status_id, description) VALUES (?, ?, ?, ?)";
    private static final String QUERY_GET_STATUS_ID = "SELECT id FROM status WHERE status_name = ?";

    private static final String QUERY_GET_BY_STATUS = "SELECT request.*, status.status_name FROM " +
            "request JOIN status ON status_id = status.id WHERE status.status_name = ? ORDER BY ? LIMIT ?, ?";

    private static final String QUERY_GET_BY_CLIENT = "SELECT request.*, status.status_name FROM request " +
            "JOIN status ON status_id = status.id WHERE client_id = ? ORDER BY ? LIMIT ?, ?";

    private static final String QUERY_GET_BY_MASTER = "SELECT request.*, status.status_name FROM request " +
            "JOIN status ON status_id = status.id WHERE master_id = ? ORDER BY ? LIMIT ?, ?";

    private static final String QUERY_GET_ALL = "SELECT request.*, status.status_name FROM request JOIN " +
            "status ON status_id = status.id ORDER BY ? LIMIT ?, ?";

    private static final String QUERY_GET_BY_ID = "SELECT request.*, status.status_name FROM request JOIN " +
            "status ON status_id = status.id WHERE request.id = ?";

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
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        }
    }

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
                throw new DBException(ErrorMessages.DB_EMPTY_RESULT_SET,
                        ErrorMessages.DB_NOT_FOUND);
            }
            return getRequestInstance(resultSet);
        } catch (SQLException ex) {
            String message = "Cannot get Request#" + id;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

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
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

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
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

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
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

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
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    @Override
    public List<RequestData> getEntityList(String query) throws DBException {
        logger.trace("getEntityListManager#query : {}", query);
        List<RequestData> requests = new ArrayList<>();
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Request req = getRequestInstance(resultSet);
                String clientLogin = resultSet.getString("client_login");
                String masterLogin = resultSet.getString("master_login");
                requests.add(new RequestData(req, clientLogin, masterLogin));
            }
            return requests;
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

    private int getStatusId(Request.Status status) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_STATUS_ID)) {
            statement.setString(1, status.name());
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("Cannot get id of Status {}", status);
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ErrorMessages.DB_EMPTY_RESULT_SET,
                        ErrorMessages.DB_NOT_FOUND);
            }
            return resultSet.getInt("id");
        } catch (SQLException ex) {
            String message = "Cannot get id of Status " + status;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

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
                throw new DBException(ErrorMessages.DB_NO_KEY, ErrorMessages.DB_INTERNAL);
            }
            return resultSet.getInt(1);
        } catch (SQLException ex) {
            String message = "Request cannot be added to database";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private void updateEntity(Request req, int statusId) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            int k = 0;
            statement.setInt(++k, req.getClientId());
            statement.setTimestamp(++k, Timestamp.valueOf(req.getCreationDate()));
            statement.setInt(++k, statusId);
            statement.setString(++k, req.getDescription());
            LocalDateTime compDateTime = req.getCompletionDate();
            Timestamp compTimestamp = null;
            if (compDateTime != null) {
                compTimestamp = Timestamp.valueOf(compDateTime);
            }
            statement.setTimestamp(++k, compTimestamp);
            statement.setString(++k, req.getUserReview());
            statement.setString(++k, req.getCancelReason());
            statement.setInt(++k, req.getMasterId());
            statement.setInt(++k, req.getPrice());
            statement.setInt(++k, req.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            String message = "Request#" + req.getId() + " cannot be updated";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_NOT_FOUND, ex);
        }
    }

    private Request getRequestInstance(ResultSet resultSet) throws SQLException {
        Request req = new Request();
        req.setId(resultSet.getInt("id"));
        req.setClientId(resultSet.getInt("client_id"));
        req.setCreationDate(resultSet.getTimestamp("creation_date").toLocalDateTime());
        req.setStatus(Request.Status.valueOf(resultSet.getString("status_name")));
        req.setDescription(resultSet.getString("description"));
        Timestamp completeDate = resultSet.getTimestamp("completion_date");
        if (completeDate != null) {
            req.setCompletionDate(completeDate.toLocalDateTime());
        }
        req.setUserReview(resultSet.getString("user_review"));
        req.setCancelReason(resultSet.getString("cancel_reason"));
        req.setMasterId(resultSet.getInt("master_id"));
        req.setPrice(resultSet.getInt("price"));
        return req;
    }

}
