package com.my.database.dao.mysql;

import com.my.database.DbFieldNames;
import com.my.database.dao.AbstractDao;
import com.my.database.DBManager;
import com.my.database.dao.WalletDao;
import com.my.entities.items.Wallet;
import com.my.exceptions.DBException;
import com.my.exceptions.ExceptionMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class WalletDaoMysql extends AbstractDao implements WalletDao {

    private static final Logger logger = LogManager.getLogger();

    public WalletDaoMysql(Connection connection) {
        super(connection);
    }

    private static final String QUERY_ADD = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",
            DbFieldNames.TABLE_WALLETS,
            DbFieldNames.WALLET_USER_ID,
            DbFieldNames.WALLET_BALANCE);

    private static final String QUERY_UPDATE = String.format("UPDATE %s SET %s = ? WHERE %s = ?",
            DbFieldNames.TABLE_WALLETS,
            DbFieldNames.WALLET_BALANCE,
            DbFieldNames.ID);

    @Override
    public int addEntity(Wallet wallet) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_ADD)) {
            statement.setInt(1, wallet.getBalance());
            statement.setInt(2, wallet.getId());
            statement.executeUpdate();
            return wallet.getId();
        } catch (SQLException ex) {
            logger.error("Wallet cannot be added to database", ex);
            DBManager.rollback(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_UNEXPECTED, ex);
        }
    }

    @Override
    public void updateEntity(Wallet wallet) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            statement.setInt(1, wallet.getBalance());
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("Wallet cannot be updated", ex);
            DBManager.rollback(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        }
    }

    private static final String QUERY_DELETE = String.format("DELETE FROM %s WHERE %s = ?",
            DbFieldNames.TABLE_WALLETS,
            DbFieldNames.ID);

    @Override
    public void removeEntity(Wallet wallet) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            statement.setInt(1, wallet.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("Wallet cannot be deleted", ex);
            DBManager.rollback(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        }
    }

    private static final String QUERY_GET_BY_ID = String.format("SELECT * FROM %s WHERE %s = ?",
            DbFieldNames.TABLE_WALLETS,
            DbFieldNames.ID);

    @Override
    public Wallet getEntityById(int id) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_ID)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("ResultSet is empty. Cannot get Wallet");
                DBManager.rollback(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_UNEXPECTED);
            }
            int balance = resultSet.getInt(DbFieldNames.WALLET_BALANCE);
            return new Wallet(id, balance);
        } catch (SQLException ex) {
            logger.error("Cannot get Wallet with given id", ex);
            DBManager.rollback(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    @Override
    public Wallet getEntityByUser(int userId) throws DBException {
        return getEntityById(userId);
    }

}
