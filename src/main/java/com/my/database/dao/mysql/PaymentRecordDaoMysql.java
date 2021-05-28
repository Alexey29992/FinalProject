package com.my.database.dao.mysql;

import com.my.database.DbFieldNames;
import com.my.database.dao.AbstractDao;
import com.my.database.DBManager;
import com.my.database.dao.PaymentRecordDao;
import com.my.entities.items.PaymentRecord;
import com.my.exceptions.DBException;
import com.my.exceptions.ExceptionMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentRecordDaoMysql extends AbstractDao implements PaymentRecordDao {

    private static final Logger logger = LogManager.getLogger();

    public PaymentRecordDaoMysql(Connection connection) {
        super(connection);
    }

    private static final String QUERY_ADD = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
            DbFieldNames.TABLE_PAYMENT_RECORDS,
            DbFieldNames.PAYMENT_RECORD_DATE,
            DbFieldNames.PAYMENT_RECORD_SUM,
            DbFieldNames.PAYMENT_RECORD_DESTINATION,
            DbFieldNames.PAYMENT_RECORD_WALLET_ID);

    @Override
    public int addEntity(PaymentRecord paymentRecord) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_ADD, Statement.RETURN_GENERATED_KEYS)) {
            int k = 0;
            statement.setTimestamp(++k, Timestamp.valueOf(paymentRecord.getDate()));
            statement.setInt(++k, paymentRecord.getSum());
            statement.setString(++k, paymentRecord.getDestination());
            statement.setInt(++k, paymentRecord.getWalletId());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (!resultSet.next()) {
                logger.error("ResultSet is empty. Cannot get PaymentRecord id");
                DBManager.rollback(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_UNEXPECTED);
            }
            return resultSet.getInt(1);
        } catch (SQLException ex) {
            logger.error("PaymentRecord cannot be added to database", ex);
            DBManager.rollback(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_UNEXPECTED, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_UPDATE = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
            DbFieldNames.TABLE_PAYMENT_RECORDS,
            DbFieldNames.PAYMENT_RECORD_DATE,
            DbFieldNames.PAYMENT_RECORD_SUM,
            DbFieldNames.PAYMENT_RECORD_DESTINATION,
            DbFieldNames.PAYMENT_RECORD_WALLET_ID,
            DbFieldNames.ID);

    @Override
    public void updateEntity(PaymentRecord paymentRecord) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            int k = 0;
            statement.setTimestamp(++k, Timestamp.valueOf(paymentRecord.getDate()));
            statement.setInt(++k, paymentRecord.getSum());
            statement.setString(++k, paymentRecord.getDestination());
            statement.setInt(++k, paymentRecord.getWalletId());
            statement.setInt(++k, paymentRecord.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("PaymentRecord cannot be updated", ex);
            DBManager.rollback(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        }
    }

    private static final String QUERY_DELETE = String.format("DELETE FROM %s WHERE %s = ?",
            DbFieldNames.TABLE_PAYMENT_RECORDS,
            DbFieldNames.ID);

    @Override
    public void removeEntity(PaymentRecord paymentRecord) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            statement.setInt(1, paymentRecord.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("PaymentRecord cannot be deleted", ex);
            DBManager.rollback(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        }
    }

    private static final String QUERY_GET_BY_ID = String.format("SELECT * FROM %s WHERE %s = ?",
            DbFieldNames.TABLE_PAYMENT_RECORDS,
            DbFieldNames.ID);

    @Override
    public PaymentRecord getEntityById(int id) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_ID)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("ResultSet is empty. Cannot get PaymentRecord");
                DBManager.rollback(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_UNEXPECTED);
            }
            return getPaymentRecordInstance(resultSet);
        } catch (SQLException ex) {
            logger.error("Cannot get PaymentRecord with given id", ex);
            DBManager.rollback(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_BY_WALLET_ID = String.format(
            "SELECT * FROM %s WHERE %s = ? ORDER BY ? LIMIT ?, ?",
            DbFieldNames.TABLE_PAYMENT_RECORDS,
            DbFieldNames.PAYMENT_RECORD_WALLET_ID);

    @Override
    public List<PaymentRecord> getEntityList(int walletId, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        List<PaymentRecord> paymentRecords = new ArrayList<>();
        ResultSet resultSet = null;
        int startPos = chunkNumber * chunkSize;
        int endPos = startPos + chunkSize;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_WALLET_ID)) {
            int k = 0;
            statement.setInt(++k, walletId);
            statement.setString(++k, sortingFactor);
            statement.setInt(++k, startPos);
            statement.setInt(++k, endPos);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                paymentRecords.add(getPaymentRecordInstance(resultSet));
            }
            return paymentRecords;
        } catch (SQLException ex) {
            logger.error("PaymentRecord cannot be deleted", ex);
            DBManager.rollback(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private PaymentRecord getPaymentRecordInstance(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(DbFieldNames.ID);
        int sum = resultSet.getInt(DbFieldNames.PAYMENT_RECORD_SUM);
        int walletId = resultSet.getInt(DbFieldNames.PAYMENT_RECORD_WALLET_ID);
        LocalDateTime date = resultSet.getTimestamp(DbFieldNames.PAYMENT_RECORD_DATE).toLocalDateTime();
        String dest = resultSet.getString(DbFieldNames.PAYMENT_RECORD_DESTINATION);
        return new PaymentRecord(id, sum, walletId, date, dest);
    }

}
