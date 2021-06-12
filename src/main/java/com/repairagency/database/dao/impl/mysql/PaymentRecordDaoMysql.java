package com.repairagency.database.dao.impl.mysql;

import com.repairagency.database.DBManager;
import com.repairagency.bean.data.PaymentRecord;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class PaymentRecordDaoMysql extends AbstractDao<PaymentRecord> {

    private static final Logger logger = LogManager.getLogger();

    private static final String QUERY_ADD = "INSERT INTO payment_record (date, sum," +
            " destination, client_id) VALUES (?, ?, ?, ?)";
    private static final String QUERY_UPDATE = "UPDATE payment_record SET date = ?," +
            " sum = ?, destination = ?, client_id = ? WHERE id = ?";
    private static final String QUERY_DELETE = "DELETE FROM payment_record WHERE id = ?";
    public static final String GET_BASE = "SELECT * FROM payment_record";

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
    protected PaymentRecord getInstance(ResultSet resultSet) throws SQLException {
        PaymentRecord pr = new PaymentRecord();
        pr.setId(resultSet.getInt("id"));
        pr.setSum(resultSet.getInt("sum"));
        pr.setClientId(resultSet.getInt("client_id"));
        pr.setDate(resultSet.getTimestamp("date").toLocalDateTime());
        pr.setDestination(resultSet.getString("destination"));
        return pr;
    }

    @Override
    protected String getQueryBase() {
        return GET_BASE;
    }

}
