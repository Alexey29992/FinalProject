package com.repairagency.database.dao.mysql;

import com.repairagency.database.DbNames;
import com.repairagency.database.dao.AbstractDao;
import com.repairagency.database.DBManager;
import com.repairagency.database.dao.PaymentRecordDao;
import com.repairagency.entities.beans.PaymentRecord;
import com.repairagency.exceptions.DBException;
import com.repairagency.exceptions.ExceptionMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentRecordDaoMysql extends AbstractDao implements PaymentRecordDao {

    private static final Logger logger = LogManager.getLogger();

    public PaymentRecordDaoMysql(Connection connection) {
        super(connection);
    }

    private static final String QUERY_ADD = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
            DbNames.TABLE_PAYMENT_RECORDS,
            DbNames.PAYMENT_RECORD_DATE,
            DbNames.PAYMENT_RECORD_SUM,
            DbNames.PAYMENT_RECORD_DESTINATION,
            DbNames.PAYMENT_RECORD_WALLET_ID);

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
                logger.error("Cannot receive PaymentRecord id");
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_NO_KEY, ExceptionMessages.DB_INTERNAL);
            }
            return resultSet.getInt(1);
        } catch (SQLException ex) {
            String message = "PaymentRecord cannot be added to database";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_UPDATE = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
            DbNames.TABLE_PAYMENT_RECORDS,
            DbNames.PAYMENT_RECORD_DATE,
            DbNames.PAYMENT_RECORD_SUM,
            DbNames.PAYMENT_RECORD_DESTINATION,
            DbNames.PAYMENT_RECORD_WALLET_ID,
            DbNames.ID);

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
            String message = "PaymentRecord#" + paymentRecord.getId() + " cannot be updated";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        }
    }

    private static final String QUERY_DELETE = String.format("DELETE FROM %s WHERE %s = ?",
            DbNames.TABLE_PAYMENT_RECORDS,
            DbNames.ID);

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
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        }
    }

    private static final String QUERY_GET_BY_ID = String.format("SELECT * FROM %s WHERE %s = ?",
            DbNames.TABLE_PAYMENT_RECORDS,
            DbNames.ID);

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
                throw new DBException(ExceptionMessages.DB_EMPTY_RESULT_SET,
                        ExceptionMessages.DB_NOT_FOUND);
            }
            return getPaymentRecordInstance(resultSet);
        } catch (SQLException ex) {
            String message = "Cannot get PaymentRecord#" + id;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_BY_WALLET_ID = String.format(
            "SELECT * FROM %s WHERE %s = ? ORDER BY ? LIMIT ?, ?",
            DbNames.TABLE_PAYMENT_RECORDS,
            DbNames.PAYMENT_RECORD_WALLET_ID);

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
            String message = "Cannot get PaymentRecords for Wallet#" + walletId;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private PaymentRecord getPaymentRecordInstance(ResultSet resultSet) throws SQLException {
        PaymentRecord pr = new PaymentRecord();
        pr.setId(resultSet.getInt(DbNames.ID));
        pr.setSum(resultSet.getInt(DbNames.PAYMENT_RECORD_SUM));
        pr.setWalletId(resultSet.getInt(DbNames.PAYMENT_RECORD_WALLET_ID));
        pr.setDate(resultSet.getTimestamp(DbNames.PAYMENT_RECORD_DATE).toLocalDateTime());
        pr.setDestination(resultSet.getString(DbNames.PAYMENT_RECORD_DESTINATION));
        return pr;
    }

}
