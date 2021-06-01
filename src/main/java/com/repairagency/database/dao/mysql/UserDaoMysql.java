package com.repairagency.database.dao.mysql;

import com.repairagency.database.DBManager;
import com.repairagency.database.DbNames;
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
        String message = "User with this role cannot be added to DB";
        logger.error(message);
        throw new DBException(message, ExceptionMessages.DB_INTERNAL);
    }

    @Override
    public void updateEntity(User user) throws DBException {
        int roleId = getRoleId(user.getRole());
        updateEntity(user, roleId);
    }

    private static final String QUERY_DELETE = String.format("DELETE FROM %s WHERE %s = ?",
            DbNames.TABLE_USERS,
            DbNames.ID);

    @Override
    public void removeEntity(User user) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            String message = "User#" + user.getId() + " cannot be deleted";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_NOT_FOUND, ex);
        }
    }

    private static final String QUERY_GET_BY_ID = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s JOIN %2$s ON %4$s = %2$s.%5$s WHERE %5$s = ?",
            DbNames.TABLE_USERS,
            DbNames.TABLE_ROLES,
            DbNames.ROLE_NAME,
            DbNames.USERS_ROLE_ID,
            DbNames.ID);

    @Override
    public User getEntityById(int id) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_ID)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("Cannot get User#{}", id);
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_EMPTY_RESULT_SET,
                        ExceptionMessages.DB_NOT_FOUND);
            }
            return getUserInstance(resultSet);
        } catch (SQLException ex) {
            String message = "Cannot get User#" + id;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_NOT_FOUND, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_BY_LOGIN = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s JOIN %2$s ON %4$s = %2$s.%5$s WHERE %6$s = ?",
            DbNames.TABLE_USERS,
            DbNames.TABLE_ROLES,
            DbNames.ROLE_NAME,
            DbNames.USERS_ROLE_ID,
            DbNames.ID,
            DbNames.USERS_LOGIN);

    @Override
    public User getEntityByLogin(String login) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_LOGIN)) {
            statement.setString(1, login);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("Cannot get User with login '{}'", login);
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_EMPTY_RESULT_SET,
                        ExceptionMessages.DB_INTERNAL);
            }
            return getUserInstance(resultSet);
        } catch (SQLException ex) {
            String message = "Cannot get User with login '" + login + "'";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_NOT_FOUND, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_ALL = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s JOIN %2$s ON %4$s = %2$s.%5$s" +
                    " ORDER BY ? LIMIT ?, ?",
            DbNames.TABLE_USERS,
            DbNames.TABLE_ROLES,
            DbNames.ROLE_NAME,
            DbNames.USERS_ROLE_ID,
            DbNames.ID);

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
            String message = "Cannot get User List";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_NOT_FOUND, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_GET_BY_ROLE = String.format(
            "SELECT %1$s.*, %2$s.%3$s FROM %1$s JOIN %2$s ON %4$s = %2$s.%5$s" +
                    " WHERE %6$s = ? ORDER BY ? LIMIT ?, ?",
            DbNames.TABLE_USERS,
            DbNames.TABLE_ROLES,
            DbNames.ROLE_NAME,
            DbNames.USERS_ROLE_ID,
            DbNames.ID,
            DbNames.ROLE_NAME);

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
            String message = "Cannot get User List for Role " + role;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_NOT_FOUND, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private int addManager(Manager manager) throws DBException {
        return addUser(manager);
    }

    private int addMaster(Master master) throws DBException {
        int id = addUser(master);
        addId(id, DbNames.TABLE_MASTERS);
        return id;
    }

    private static final String QUERY_ADD_ID = String.format(
            "INSERT INTO ? (%s) VALUES (?)",
            DbNames.ID
    );

    private void addId(int id, String tableName) throws DBException {
        try (PreparedStatement statement = connection.
                prepareStatement(QUERY_ADD_ID)) {
            statement.setString(1, tableName);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            String message = "Id#" + id + " cannot be inserted into " + tableName;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        }
    }

    private int addClient(Client client) throws DBException {
        int id = addUser(client);
        addId(id, DbNames.TABLE_USERS);
        return id;
    }

    private int addUser(User user) throws DBException {
        int roleId = getRoleId(user.getRole());
        return addUser(user, roleId);
    }

    private static final String QUERY_ADD_USER = String.format(
            "INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)",
            DbNames.TABLE_USERS,
            DbNames.USERS_LOGIN,
            DbNames.USERS_PASSWORD,
            DbNames.USERS_ROLE_ID
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
                logger.error("Cannot receive User id");
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_NO_KEY, ExceptionMessages.DB_INTERNAL);
            }
            return resultSet.getInt(1);
        } catch (SQLException ex) {
            String message = "User cannot be added to database";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private static final String QUERY_UPDATE = String.format(
            "UPDATE %s SET %s = ?, %s = ? %s = ? WHERE %s = ?",
            DbNames.TABLE_USERS,
            DbNames.USERS_LOGIN,
            DbNames.USERS_PASSWORD,
            DbNames.USERS_ROLE_ID,
            DbNames.ID);

    private void updateEntity(User user, int roleId) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            int k = 0;
            statement.setString(++k, user.getLogin());
            statement.setString(++k, user.getPassword());
            statement.setInt(++k, roleId);
            statement.setInt(++k, user.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            String message = "User#" + user.getId() + " cannot be updated";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        }
    }

    private static final String QUERY_GET_ROLE_ID = String.format("SELECT %s FROM %s WHERE %s = ?",
            DbNames.ID,
            DbNames.TABLE_ROLES,
            DbNames.ROLE_NAME);

    private int getRoleId(Role role) throws DBException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_ROLE_ID)) {
            statement.setString(1, role.name());
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("Cannot get id of Role {}", role);
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ExceptionMessages.DB_EMPTY_RESULT_SET,
                        ExceptionMessages.DB_INTERNAL);
            }
            return resultSet.getInt(DbNames.ID);
        } catch (SQLException ex) {
            String message = "Cannot get id of Role " + role;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ExceptionMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private User getUserInstance(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(DbNames.ID);
        String login = resultSet.getString(DbNames.USERS_LOGIN);
        String password = resultSet.getString(DbNames.USERS_PASSWORD);
        Role role = Role.valueOf(resultSet.getString(DbNames.ROLE_NAME));
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
