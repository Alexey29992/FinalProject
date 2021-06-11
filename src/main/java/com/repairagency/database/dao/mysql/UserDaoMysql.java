package com.repairagency.database.dao.mysql;

import com.repairagency.database.DBManager;
import com.repairagency.database.dao.AbstractDao;
import com.repairagency.database.dao.UserDao;
import com.repairagency.entity.User;
import com.repairagency.entity.user.Admin;
import com.repairagency.entity.user.Client;
import com.repairagency.entity.user.Manager;
import com.repairagency.entity.user.Master;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoMysql extends AbstractDao implements UserDao {

    private static final Logger logger = LogManager.getLogger();

    private static final String QUERY_DELETE = "DELETE FROM user WHERE id = ?";
    private static final String QUERY_GET_BY_ID =
            "SELECT user.*, role.role_name, client.ph_number, client.balance FROM user JOIN role ON " +
                    "role_id = role.id JOIN client ON user.id = client.id WHERE user.id = ?";
    private static final String QUERY_GET_BY_LOGIN =
            "SELECT user.*, role.role_name, client.ph_number, client.balance FROM user JOIN role ON " +
                    "role_id = role.id JOIN client ON user.id = client.id WHERE user.login = ?";
    private static final String QUERY_GET_ALL = "SELECT user.*, role.role_name FROM" +
            " user JOIN role ON role_id = role.id ORDER BY ? LIMIT ?, ?";
    private static final String QUERY_GET_BY_ROLE =
            "SELECT user.*, role.role_name FROM user JOIN role ON role_id = role.id" +
                    " WHERE role_name = ? ORDER BY ? LIMIT ?, ?";
    private static final String QUERY_ADD_USER = "INSERT INTO user (login, password, role_id) VALUES (?, ?, ?)";
    private static final String QUERY_ADD_ID_CLIENT = "INSERT INTO client (id) VALUE (?)";
    private static final String QUERY_ADD_ID_MASTER = "INSERT INTO master (id) VALUE (?)";
    private static final String QUERY_GET_ROLE_ID = "SELECT id FROM role WHERE role_name = ?";
    private static final String QUERY_UPDATE_USER =
            "UPDATE user SET login = ?, password = ?, role_id = ? WHERE id = ?";
    private static final String QUERY_UPDATE_CLIENT = "UPDATE client SET ph_number = ?, balance = ? WHERE id = ?";

    public UserDaoMysql(Connection connection) {
        super(connection);
    }

    @Override
    public int addEntity(User user) throws DBException {
        logger.debug("Adding user");
        User.Role role = user.getRole();
        int roleId = getRoleId(role);
        int userId = addUser(user, roleId);
        user.setId(userId);
        if (user instanceof Client) {
            addClientSpecific((Client) user);
            return userId;
        }
        if (user instanceof Master) {
            addMasterSpecific((Master) user);
            return userId;
        }
        if (user instanceof Manager) {
            return userId;
        }
        String message = "User with this role cannot be added to DB";
        logger.error(message);
        throw new DBException(message, ErrorMessages.DB_INTERNAL);
    }

    @Override
    public void updateEntity(User user) throws DBException {
        logger.debug("Updating user");
        int roleId = getRoleId(user.getRole());
        updateUser(user, roleId);
        if (user instanceof Client) {
            updateClientSpecific((Client) user);
        }
    }

    @Override
    public void removeEntity(User user) throws DBException {
        logger.debug("Removing user");
        try (PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            String message = "User#" + user.getId() + " cannot be deleted";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        }
    }

    @Override
    public User getEntityById(int id) throws DBException {
        logger.debug("Querying user by id");
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_ID)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("Cannot get User#{}", id);
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ErrorMessages.DB_EMPTY_RESULT_SET,
                        ErrorMessages.DB_NOT_FOUND);
            }

            User user = getUserInstance(resultSet);
            logger.trace("USER: {}", user);
            return user;
        } catch (SQLException ex) {
            String message = "Cannot get User#" + id;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    @Override
    public User getEntityByLogin(String login) throws DBException {
        logger.debug("Querying user by login");
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_BY_LOGIN)) {
            statement.setString(1, login);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.trace("Cannot get User with login '{}'", login);
                return null;
            }
            User user = getUserInstance(resultSet);
            logger.trace("Role : {}", user.getRole());
            return user;
        } catch (SQLException ex) {
            String message = "Cannot get User with login '" + login + "'";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_NOT_FOUND, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    @Override
    public List<User> getEntityListAll(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        logger.debug("Querying user list");
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
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    public List<User> getEntityListByRole(User.Role role, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        logger.debug("Querying user list by role");
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
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    ////////////////////////////////////////////////////////

    private int addUser(User user, int roleId) throws DBException {
        logger.debug("Adding user with roleId");
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
                logger.error("Cannot receive User id.");
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ErrorMessages.DB_NO_KEY, ErrorMessages.DB_INTERNAL);
            }
            return resultSet.getInt(1);
        } catch (SQLException ex) {
            String message = "User cannot be added to database";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    private void addMasterSpecific(Master master) throws DBException {
        logger.debug("Adding master specific fields");
        addId(master.getId(), QUERY_ADD_ID_MASTER);
    }

    private void addClientSpecific(Client client) throws DBException {
        logger.debug("Adding client specific fields");
        addId(client.getId(), QUERY_ADD_ID_CLIENT);
    }

    private void addId(int id, String query) throws DBException {
        logger.debug("Adding id");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            String message = "Id#" + id + " cannot be inserted into 'master'/'client' table";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        }
    }

    ////////////////////////////////////////////////////////

    private void updateUser(User user, int roleId) throws DBException {
        logger.debug("Updating user with roleId");
        try (PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE_USER)) {
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
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        }
    }

    private void updateClientSpecific(Client client) throws DBException {
        logger.debug("Updating client specific fields");
        try (PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE_CLIENT)) {
            int k = 0;
            statement.setString(++k, client.getPhNumber());
            statement.setInt(++k, client.getBalance());
            statement.setInt(++k, client.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            String message = "Client#" + client.getId() + " cannot be updated";
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        }
    }

    ////////////////////////////////////////////////////////

    private int getRoleId(User.Role role) throws DBException {
        logger.debug("Querying role id");
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_ROLE_ID)) {
            statement.setString(1, role.name());
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                logger.error("Cannot get id of Role {}", role);
                DBManager.rollbackTransaction(connection);
                DBManager.closeConnection(connection);
                throw new DBException(ErrorMessages.DB_EMPTY_RESULT_SET,
                        ErrorMessages.DB_INTERNAL);
            }
            return resultSet.getInt("id");
        } catch (SQLException ex) {
            String message = "Cannot get id of Role " + role;
            logger.error(message, ex);
            DBManager.rollbackTransaction(connection);
            DBManager.closeConnection(connection);
            throw new DBException(message, ErrorMessages.DB_INTERNAL, ex);
        } finally {
            closeResultSet(resultSet);
        }
    }

    ////////////////////////////////////////////////////////

    private User getUserInstance(ResultSet resultSet) throws SQLException {
        logger.debug("Constructing user instance");
        int id = resultSet.getInt("id");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        User.Role role = User.Role.valueOf(resultSet.getString("role_name"));
        String phone = resultSet.getString("ph_number");
        int balance = resultSet.getInt("balance");
        logger.trace("User role : {}", role);
        switch (role) {
            case CLIENT:
                Client client = new Client(id, login, password);
                client.setPhNumber(phone);
                client.setBalance(balance);
                return client;
            case MASTER:
                return new Master(id, login, password);
            case MANAGER:
                return new Manager(id, login, password);
            case ADMIN:
                return new Admin(id, login, password);
            default:
                logger.fatal("User with illegal role was received from DB. Illegal role was received");
                throw new IllegalStateException(ErrorMessages.DB_FATAL);
        }
    }

}
