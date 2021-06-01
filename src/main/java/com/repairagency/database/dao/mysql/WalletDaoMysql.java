package com.repairagency.database.dao.mysql;

import com.repairagency.database.DbNames;
import com.repairagency.database.dao.AbstractDao;
import com.repairagency.database.DBManager;
import com.repairagency.database.dao.WalletDao;
import com.repairagency.entities.items.Wallet;
import com.repairagency.exceptions.DBException;
import com.repairagency.exceptions.ExceptionMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class WalletDaoMysql extends AbstractDao implements WalletDao {

    private static final Logger logger = LogManager.getLogger();

    public WalletDaoMysql(Connection connection) {
        super(connection);
    }

    private static final String QUERY_ADD = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",
            DbNames.TABLE_WALLETS,
            DbNames.WALLET_USER_ID,
            DbNames.WALLET_BALANCE);

    @Override
    public int addEntity(Wallet wallet) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_ADD)) {
            statement.setInt(1, wallet.getId());
            statement.setInt(2, wallet.getBalance());
            statement.executeUpdate();
            return wallet.getId();
        } catch (SQLException ex) {
            String message = "Wallet#" + wallet.getId() + " cannot be added to database";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        }
    }

    private static final String QUERY_UPDATE = String.format("UPDATE %s SET %s = ? WHERE %s = ?",
            DbNames.TABLE_WALLETS,
            DbNames.WALLET_BALANCE,
            DbNames.ID);

    @Override
    public void updateEntity(Wallet wallet) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            statement.setInt(1, wallet.getBalance());
            statement.executeUpdate();
        } catch (SQLException ex) {
            String message = "Wallet#" + wallet.getId() + " cannot be updated";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        }
    }

    private static final String QUERY_DELETE = String.format("DELETE FROM %s WHERE %s = ?",
            DbNames.TABLE_WALLETS,
            DbNames.ID);

    @Override
    public void removeEntity(Wallet wallet) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            statement.setInt(1, wallet.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            String message = "Wallet#" + wallet.getId() + " cannot be deleted";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        }
    }

    private static final String QUERY_GET_BY_ID = String.format("SELECT * FROM %s WHERE %s = ?",
            DbNames.TABLE_WALLETS,
            DbNames.ID);

    @Override
    public Wallet getEntityById(int id) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_ID)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("ResultSet is empty. Cannot get Wallet");
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_EMPTY_RESULT_SET,
                        ExceptionMessages.DB_INTERNAL);
            }
            int balance = resultSet.getInt(DbNames.WALLET_BALANCE);
            return new Wallet(id, balance);
        } catch (SQLException ex) {
            String message = "Cannot get Wallet#" + id;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    @Override
    public Wallet getEntityByUser(int userId) throws DBException {
        return getEntityById(userId);
    }

}
