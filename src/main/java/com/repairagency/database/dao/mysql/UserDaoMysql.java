package com.repairagency.database.dao.mysql;

import com.repairagency.database.DBManager;
import com.repairagency.database.DbFieldNames;
import com.repairagency.database.dao.AbstractDao;
import com.repairagency.database.dao.UserDao;
import com.repairagency.entities.Role;
import com.repairagency.entities.User;
import com.repairagency.entities.users.Admin;
import com.repairagency.entities.users.Client;
import com.repairagency.entities.users.Manager;
import com.repairagency.entities.users.Master;
import com.repairagency.exceptions.DBException;
import com.repairagency.exceptions.ExceptionMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoMysql extends AbstractDao implements UserDao {

    private static final Logger logger = LogManager.getLogger();

    public UserDaoMysql(Connection connection) {
        super(connection);
    }

    @Override
    public int addEntity(User user) throws DBException {
        if (user instanceof Client) {
            return addClient((Client) user);
        }
        if (user instanceof Master) {
            return addMaster((Master) user);
        }
        if (user instanceof Manager) {
            return addManager((Manager) user);
        }
        logger.error("Only Client, Master or Manager can be added to DB");
        throw new DBException(ExceptionMessages.USER_ADD_INVALID_ROLE);
    }

    @Override
    public void updateEntity(User user) throws DBException {
        int roleId = getRoleId(user.getRole());
        updateEntity(user, roleId);
    }

    private static final String QUERY_UPDATE = String.format(
            "UPDATE %s SET %s = ?, %s = ? %s = ? WHERE %s = ?",
            DbFieldNames.TABLE_USERS,
            DbFieldNames.USERS_LOGIN,
            DbFieldNames.USERS_PASSWORD,
            DbFieldNames.USERS_ROLE_ID,
            DbFieldNames.ID);

    private void updateEntity(User user, int roleId) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            int k = 0;
            statement.setString(++k, user.getLogin());
            statement.setString(++k, user.getPassword());
            statement.setInt(++k, roleId);
            statement.setInt(++k, user.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("User cannot be updated", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        }
    }

    private static final String QUERY_DELETE = String.format("DELETE FROM %s WHERE %s = ?",
            DbFieldNames.TABLE_USERS,
            DbFieldNames.ID);

    @Override
    public void removeEntity(User user) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("User cannot be deleted", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        }
    }

    private static final String QUERY_GET_BY_ID = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s JOIN %2$s ON %4$s = %2$s.%5$s WHERE %5$s = ?",
            DbFieldNames.TABLE_USERS,
            DbFieldNames.TABLE_ROLES,
            DbFieldNames.ROLE_NAME,
            DbFieldNames.USERS_ROLE_ID,
            DbFieldNames.ID);

    @Override
    public User getEntityById(int id) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_ID)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("ResultSet is empty. Cannot get User");
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_UNEXPECTED);
            }
            return getUserInstance(resultSet);
        } catch (SQLException ex) {
            logger.error("Cannot get User with given id", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_BY_LOGIN = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s JOIN %2$s ON %4$s = %2$s.%5$s WHERE %6$s = ?",
            DbFieldNames.TABLE_USERS,
            DbFieldNames.TABLE_ROLES,
            DbFieldNames.ROLE_NAME,
            DbFieldNames.USERS_ROLE_ID,
            DbFieldNames.ID,
            DbFieldNames.USERS_LOGIN);

    @Override
    public User getEntityByLogin(String login) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_LOGIN)) {
            statement.setString(1, login);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("ResultSet is empty. Cannot get User");
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_UNEXPECTED);
            }
            return getUserInstance(resultSet);
        } catch (SQLException ex) {
            logger.error("Cannot get User with given id", ex);
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
            DbFieldNames.TABLE_USERS,
            DbFieldNames.TABLE_ROLES,
            DbFieldNames.ROLE_NAME,
            DbFieldNames.USERS_ROLE_ID,
            DbFieldNames.ID);

    @Override
    public List<User> getEntityListAll(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        List<User> users = new ArrayList<>();
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
                users.add(getUserInstance(resultSet));
            }
            return users;
        } catch (SQLException ex) {
            logger.error("Cannot get Request list", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_BY_ROLE = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s JOIN %2$s ON %4$s = %2$s.%5$s" +
                    " WHERE %6$s = ? ORDER BY ? LIMIT ?, ?",
            DbFieldNames.TABLE_USERS,
            DbFieldNames.TABLE_ROLES,
            DbFieldNames.ROLE_NAME,
            DbFieldNames.USERS_ROLE_ID,
            DbFieldNames.ID,
            DbFieldNames.ROLE_NAME);

    @Override
    public List<User> getEntityListByRole(Role role, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        List<User> users = new ArrayList<>();
        ResultSet resultSet = null;
        int startPos = chunkNumber * chunkSize;
        int endPos = startPos + chunkSize;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_ROLE)) {
            int k = 0;
            statement.setString(++k, role.name());
            statement.setString(++k, sortingFactor);
            statement.setInt(++k, startPos);
            statement.setInt(++k, endPos);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(getUserInstance(resultSet));
            }
            return users;
        } catch (SQLException ex) {
            logger.error("Cannot get Request list", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private int addManager(Manager manager) throws DBException {
        return addUser(manager);
    }

    private int addMaster(Master master) throws DBException {
        int id = addUser(master);
        addMasterId(id);
        return id;
    }

    private static final String QUERY_ADD_MASTER = String.format(
            "INSERT INTO %s (%s) VALUES (?)",
            DbFieldNames.TABLE_MASTERS,
            DbFieldNames.ID
    );

    private void addMasterId(int id) throws DBException {
        try (PreparedStatement statement = connection.
                prepareStatement(QUERY_ADD_MASTER)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("Master cannot be added to database", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_UNEXPECTED, ex);
        }
    }

    private int addClient(Client client) throws DBException {
        int id = addUser(client);
        addClientId(id);
        return id;
    }

    private static final String QUERY_ADD_CLIENT = String.format(
            "INSERT INTO %s (%s) VALUES (?)",
            DbFieldNames.TABLE_CLIENTS,
            DbFieldNames.ID
    );

    private void addClientId(int id) throws DBException {
        try (PreparedStatement statement = connection.
                prepareStatement(QUERY_ADD_CLIENT)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("Master cannot be added to database", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_UNEXPECTED, ex);
        }
    }

    private int addUser(User user) throws DBException {
        int roleId = getRoleId(user.getRole());
        return addUser(user, roleId);
    }

    private static final String QUERY_ADD_USER = String.format(
            "INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)",
            DbFieldNames.TABLE_USERS,
            DbFieldNames.USERS_LOGIN,
            DbFieldNames.USERS_PASSWORD,
            DbFieldNames.USERS_ROLE_ID
    );

    private int addUser(User user, int roleId) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.
                prepareStatement(QUERY_ADD_USER, Statement.RETURN_GENERATED_KEYS)) {
            int k = 0;
            statement.setString(++k, user.getLogin());
            statement.setString(++k, user.getPassword());
            statement.setInt(++k, roleId);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (!resultSet.next()) {
                logger.error("ResultSet is empty. Cannot get User id");
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_UNEXPECTED);
            }
            return resultSet.getInt(1);
        } catch (SQLException ex) {
            logger.error("User cannot be added to database", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_UNEXPECTED, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_ROLE_ID = String.format("SELECT %s FROM %s WHERE %s = ?",
            DbFieldNames.ID,
            DbFieldNames.TABLE_ROLES,
            DbFieldNames.ROLE_NAME);

    private int getRoleId(Role role) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_ROLE_ID)) {
            statement.setString(1, role.name());
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("ResultSet is empty. Cannot get id of queried Role");
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_UNEXPECTED);
            }
            return resultSet.getInt(DbFieldNames.ID);
        } catch (SQLException ex) {
            logger.error("Cannot get id of the queried Role", ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(ExceptionMessages.DB_INVALID_QUERY, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private User getUserInstance(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(DbFieldNames.ID);
        String login = resultSet.getString(DbFieldNames.USERS_LOGIN);
        String password = resultSet.getString(DbFieldNames.USERS_PASSWORD);
        Role role = Role.valueOf(resultSet.getString(DbFieldNames.ROLE_NAME));
        switch (role) {
            case CLIENT:
                return new Client(id, login, password);
            case MASTER:
                return new Master(id, login, password);
            case MANAGER:
                return new Manager(id, login, password);
            case ADMIN:
                return new Admin(id, login, password);
            default:
                logger.fatal("User with illegal role was received from DB. Illegal role was received");
                throw new IllegalStateException(ExceptionMessages.DB_FATAL);
        }
    }

}
