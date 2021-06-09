package com.repairagency.database.dao.mysql;

import com.repairagency.database.dao.AbstractDao;
import com.repairagency.database.DBManager;
import com.repairagency.database.dao.PaymentRecordDao;
import com.repairagency.entity.bean.PaymentRecord;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentRecordDaoMysql extends AbstractDao implements PaymentRecordDao {

    private static final Logger logger = LogManager.getLogger();

    private static final String QUERY_ADD = "INSERT INTO payment_record (date, sum," +
            " destination, client_id) VALUES (?, ?, ?, ?)";
    private static final String QUERY_UPDATE = "UPDATE payment_record SET date = ?," +
            " sum = ?, destination = ?, client_id = ? WHERE id = ?";
    private static final String QUERY_DELETE = "DELETE FROM payment_record WHERE id = ?";
    private static final String QUERY_GET_BY_ID = "SELECT * FROM payment_record WHERE id = ?";
    private static final String QUERY_GET_BY_CLIENT_ID = "SELECT * FROM payment_record WHERE" +
            " client_id = ? ORDER BY ? LIMIT ?, ?";

    public PaymentRecordDaoMysql(Connection connection) {
        super(connection);
    }

    @Override
    public int addEntity(PaymentRecord paymentRecord) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_ADD, Statement.RETURN_GENERATED_KEYS)) {
            int k = 0;
            statement.setTimestamp(++k, Timestamp.valueOf(paymentRecord.getDate()));
            statement.setInt(++k, paymentRecord.getSum());
            statement.setString(++k, paymentRecord.getDestination());
            statement.setInt(++k, paymentRecord.getClientId());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (!resultSet.next()) {
                logger.error("Cannot receive PaymentRecord id");
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ErrorMessages.DB_NO_KEY, ErrorMessages.DB_INTERNAL);
            }
            return resultSet.getInt(1);
        } catch (SQLException ex) {
            String message = "PaymentRecord cannot be added to database";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    @Override
    public void updateEntity(PaymentRecord paymentRecord) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            int k = 0;
            statement.setTimestamp(++k, Timestamp.valueOf(paymentRecord.getDate()));
            statement.setInt(++k, paymentRecord.getSum());
            statement.setString(++k, paymentRecord.getDestination());
            statement.setInt(++k, paymentRecord.getClientId());
            statement.setInt(++k, paymentRecord.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            String message = "PaymentRecord#" + paymentRecord.getId() + " cannot be updated";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        }
    }

    @Override
    public void removeEntity(PaymentRecord paymentRecord) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            statement.setInt(1, paymentRecord.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            String message = "PaymentRecord#" + paymentRecord.getId() + " cannot be deleted";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        }
    }

    @Override
    public PaymentRecord getEntityById(int id) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_ID)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("Cannot get PaymentRecord#{}", id);
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ErrorMessages.DB_EMPTY_RESULT_SET,
                        ErrorMessages.DB_NOT_FOUND);
            }
            return getPaymentRecordInstance(resultSet);
        } catch (SQLException ex) {
            String message = "Cannot get PaymentRecord#" + id;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    @Override
    public List<PaymentRecord> getEntityList(int clientId, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        List<PaymentRecord> paymentRecords = new ArrayList<>();
        ResultSet resultSet = null;
        int startPos = chunkNumber * chunkSize;
        int endPos = startPos + chunkSize;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_CLIENT_ID)) {
            int k = 0;
            statement.setInt(++k, clientId);
            statement.setString(++k, sortingFactor);
            statement.setInt(++k, startPos);
            statement.setInt(++k, endPos);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                paymentRecords.add(getPaymentRecordInstance(resultSet));
            }
            return paymentRecords;
        } catch (SQLException ex) {
            String message = "Cannot get PaymentRecords for Client#" + clientId;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private PaymentRecord getPaymentRecordInstance(ResultSet resultSet) throws SQLException {
        PaymentRecord pr = new PaymentRecord();
        pr.setId(resultSet.getInt("id"));
        pr.setSum(resultSet.getInt("sum"));
        pr.setClientId(resultSet.getInt("client_id"));
        pr.setDate(resultSet.getTimestamp("date").toLocalDateTime());
        pr.setDestination(resultSet.getString("destination"));
        return pr;
    }

}
