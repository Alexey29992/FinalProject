package com.repairagency.bean;

import com.repairagency.bean.data.PaymentRecord;
import com.repairagency.bean.data.Request;
import com.repairagency.database.QueryGetData;
import com.repairagency.text.Text;
import com.repairagency.database.DBManager;
import com.repairagency.database.dao.*;
import com.repairagency.bean.user.Client;
import com.repairagency.bean.user.Manager;
import com.repairagency.bean.user.Master;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.util.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class that contains top level methods to work with
 * model. Methods of this class manages database connection
 * and works with DAO directly.
 */
public class EntityManager {

    private static final Logger logger = LogManager.getLogger();

    private EntityManager() {
    }

    /**
     * Receives {@link List} of PaymentRecord entities according to given {@link QueryGetData} object.
     * @param queryData QueryGetData object that holds the data required to generate desired SQL query
     * @return List of PaymentRecords according to given query (can be empty)
     * @throws DBException if given queryData not correct or internal database errors occur
     */
    public static List<PaymentRecord> getPaymentRecordList(QueryGetData queryData)
            throws DBException {
        Connection connection = startTransaction();
        List<PaymentRecord> requests = getPaymentRecordList(queryData, connection);
        completeTransaction(connection);
        return requests;
    }

    /**
     * Creates new {@link Request} with given description for {@link Client}
     * with given ID and inserts it in the database.
     * @param description the description of new Request
     * @param clientId Client ID for which new Request will be created
     * @throws DBException if internal database errors occur
     */
    public static void newRequest(String description, int clientId) throws DBException {
        logger.debug("Creating new request from Client#{}", clientId);
        Request request = new Request(description, clientId);
        Connection connection = startTransaction();
        Dao<Request> dao = getRequestDao(connection);
        int id = dao.addEntity(request);
        request.setId(id);
        completeTransaction(connection);
    }

    /**
     * Receives single {@link Request} instance from the database.
     * @param id ID of Request that will be received
     * @return Request instance with given ID
     * @throws DBException if internal database errors occur
     * @throws InvalidOperationException if Request with given ID doesn't exist
     */
    public static Request getRequest(int id)
            throws DBException, InvalidOperationException {
        Connection connection = startTransaction();
        Request request = getRequest(id, connection);
        completeTransaction(connection);
        return request;
    }

    /**
     * Receives {@link List} of {@link Request} entities according to given {@link QueryGetData} object.
     * @param queryData QueryGetData object that holds the data required to generate desired SQL query
     * @return List of Requests according to given query (can be empty)
     * @throws DBException if given queryData not correct or internal database errors occur
     */
    public static List<Request> getRequestList(QueryGetData queryData) throws DBException {
        Connection connection = startTransaction();
        List<Request> requests = getRequestList(queryData, connection);
        completeTransaction(connection);
        return requests;
    }

    /**
     * Updates given {@link Request} in the database according to input Request data
     * if it exists. Does nothing if given entity doesn't exist in the database.
     * @param request Request instance that holds data to be updated in the database
     * @throws DBException if internal database errors occur
     */
    public static void updateRequest(Request request) throws DBException {
        Connection connection = startTransaction();
        getRequestDao(connection).updateEntity(request);
        completeTransaction(connection);
    }

    /**
     * Removes {@link Request} with given ID from the database if it exists.
     * Does nothing if Request with given ID doesn't exists.
     * @param id ID of Request to be removed
     * @throws DBException if internal database errors occur
     */
    public static void removeRequest(int id) throws DBException {
        Connection connection = startTransaction();
        Dao<Request> dao = getRequestDao(connection);
        dao.removeEntity(id);
        completeTransaction(connection);
    }

    /**
     * Creates new {@link User} with given role
     * Login, password and role given to this method should be valid.
     * @param login login of new User
     * @param password password of new User
     * @param role Role of new User
     * @return created User instance
     * @throws InvalidOperationException if Client with such login already exists or Role not valid
     * @throws DBException if database errors occur
     */
    public static User newUser(String login, String password, User.Role role)
            throws DBException, InvalidOperationException {
        logger.debug("Creating new {} with login '{}'", role, login);
        QueryGetData queryData = new QueryGetData();
        queryData.setFilterFactor("user.login", login);
        List<User> users = getUserList(queryData);
        if (!users.isEmpty()) {
            logger.debug("Can not register: login is already registered");
            throw new InvalidOperationException(ErrorMessages.USER_CREATE_LOGIN_REGISTERED);
        }
        User user;
        switch (role) {
            case CLIENT:
                user = new Client(login, password);
                break;
            case MASTER:
                user = new Master(login, password);
                break;
            case MANAGER:
                user = new Manager(login, password);
                break;
            default:
                logger.error("User role not valid");
                throw new InvalidOperationException(ErrorMessages.USER_CREATE_INVALID_ROLE);
        }
        Connection connection = startTransaction();
        Dao<User> userDao = getUserDao(connection);
        int clientId = userDao.addEntity(user);
        completeTransaction(connection);
        user.setId(clientId);
        logger.trace("Successfully created {} with id = {}", role, user.getId());
        return user;
    }

    /**
     * Receives single {@link User} instance from the database.
     * @param id ID of User that will be received
     * @return User instance with given ID
     * @throws DBException if internal database errors occur
     * @throws InvalidOperationException if User with given ID doesn't exist
     */
    public static User getUser(int id)
            throws DBException, InvalidOperationException {
        Connection connection = startTransaction();
        User user = getUser(id, connection);
        completeTransaction(connection);
        return user;
    }

    /**
     * Receives single {@link User} instance from the database.
     * @param login login of User that will be received
     * @return User instance with given login
     * @throws DBException if internal database errors occur
     * @throws InvalidOperationException if User with given login doesn't exist
     */
    public static User getUser(String login)
            throws DBException, InvalidOperationException {
        Connection connection = startTransaction();
        User user = getUser(login, connection);
        completeTransaction(connection);
        return user;
    }

    /**
     * Receives {@link List} of {@link User} entities according to given {@link QueryGetData} object.
     * @param queryData QueryGetData object that holds the data required to generate desired SQL query
     * @return List of Users according to given query (can be empty)
     * @throws DBException if given queryData not correct or internal database errors occur
     */
    public static List<User> getUserList(QueryGetData queryData) throws DBException {
        Connection connection = startTransaction();
        List<User> users = getUserList(queryData, connection);
        completeTransaction(connection);
        return users;
    }

    /**
     * Receives {@link Map} of {@link Master} logins.
     * The Keys of the map are Master IDs and the values - logins
     * @return Map of all Master ID-Login entries
     * @throws DBException if internal database errors occur
     */
    public static Map<Integer, String> getMasterLogins() throws DBException {
        Connection connection = startTransaction();
        QueryGetData queryData = new QueryGetData();
        queryData.setFilterFactor("role_name", "MASTER");
        List<User> masters = EntityManager.getUserList(queryData);
        Map<Integer, String> logins = masters.stream()
                .collect(Collectors.toMap(User::getId, User::getLogin));
        completeTransaction(connection);
        return logins;
    }

    /**
     * Updates given {@link User} in the database according to input User data
     * if it exists. Does nothing if given entity doesn't exist in the database.
     * @param request User instance that holds data to be updated in the database
     * @throws DBException if internal database errors occur
     */
    public static void updateUser(User request) throws DBException {
        Connection connection = startTransaction();
        getUserDao(connection).updateEntity(request);
        completeTransaction(connection);
    }

    /**
     * Removes {@link User} with given ID from the database if it exists.
     * Does nothing if User with given ID doesn't exists.
     * @param id ID of User to be removed
     * @throws DBException if internal database errors occur
     */
    public static void removeUser(int id) throws DBException {
        Connection connection = startTransaction();
        Dao<User> dao = getUserDao(connection);
        dao.removeEntity(id);
        completeTransaction(connection);
    }

    /**
     * Replenishes balance with given amount of money of {@link Client} with given ID.
     * Additionally creates new entry in Client payment records table in the database.
     * @param clientId Client ID whose balance will be replenished
     * @param amount amount of money that will be added to Client balance.
     *               According to model logic it should be positive number
     * @return Client balance after replenishment made
     * @throws DBException if internal database errors occur
     * @throws InvalidOperationException if Client with given ID does not exists in the database
     */
    public static int topUpClientBalance(int clientId, int amount)
            throws DBException, InvalidOperationException {
        logger.debug("Top up Client balance");
        logger.trace("amount#{}$ , Client#{}", amount, clientId);
        Connection connection = startTransaction();
        Dao<User> userDao = getUserDao(connection);
        Dao<PaymentRecord> paymentRecordDao = getPaymentRecordDao(connection);

        logger.trace("Receiving Client from DB");
        Client client = (Client) getUser(clientId, connection);

        int newBalance = client.getBalance() + amount;
        client.setBalance(newBalance);

        logger.trace("Updating Client in DB");
        userDao.updateEntity(client);

        logger.trace("Creating new PaymentRecord for Client");
        PaymentRecord paymentRecord = new PaymentRecord(amount, clientId, Text.PAYMENT_RECORD_ADD_MONEY);
        paymentRecordDao.addEntity(paymentRecord);
        completeTransaction(connection);
        return newBalance;
    }

    /**
     * Processes payment of {@link Client} with given ID for {@link Request} with given ID.
     * Additionally creates new entry in Client payment records table in the database.
     * @param clientId ID of Client that makes payment
     * @param requestId ID of the Request for which Client pays
     * @return Client balance after payment made
     * @throws InvalidOperationException if Client with given ID does not exists in the database
     * @throws DBException if internal database errors occur
     */
    public static int makePayment(int clientId, int requestId)
            throws InvalidOperationException, DBException {
        logger.debug("Processing payment");
        logger.trace("Client#{} , Request#{}", clientId, requestId);
        Connection connection = startTransaction();
        Dao<User> userDao = getUserDao(connection);
        Dao<Request> requestDao = getRequestDao(connection);
        Dao<PaymentRecord> paymentRecordDao = getPaymentRecordDao(connection);

        logger.trace("Receiving Request instance from DB");
        Request request = getRequest(requestId, connection);

        logger.trace("Receiving Client from DB");
        Client client = (Client) getUser(clientId, connection);

        logger.trace("Validating payment balance : {}, price {}", client.getBalance(), request.getPrice());
        Validator.validatePayment(client, request);

        logger.trace("Updating Request");
        request.setStatus(Request.Status.PAID);
        requestDao.updateEntity(request);

        logger.trace("Creating new PaymentRecord for Client");
        PaymentRecord paymentRecord = new PaymentRecord(-request.getPrice(), clientId,
                Text.PAYMENT_RECORD_PAY_MONEY);
        paymentRecordDao.addEntity(paymentRecord);

        int newBalance = client.getBalance() - request.getPrice();
        logger.trace("Updating Client in DB");
        client.setBalance(newBalance);
        userDao.updateEntity(client);

        completeTransaction(connection);
        return newBalance;
    }

    /**
     * Receives single {@link User} instance from the database by given ID
     * on given {@link Connection} object. This method intended to for use when
     * multiple actions should be done into the same single transaction.
     * @param id ID of User that will be received
     * @param connection Connection object on which the query will be executed
     * @return User instance with given ID
     * @throws DBException if internal database errors occur
     * @throws InvalidOperationException if User with given ID doesn't exist
     */
    private static User getUser(int id, Connection connection)
            throws DBException, InvalidOperationException {
        QueryGetData queryData = new QueryGetData();
        queryData.setFilterFactor("user.id", String.valueOf(id));
        List<User> list = getUserList(queryData, connection);
        if (list.isEmpty()) {
            throw new InvalidOperationException(ErrorMessages.NO_SUCH_USER);
        }
        return list.get(0);
    }

    /**
     * Receives single {@link User} instance from the database by given login
     * on given {@link Connection} object. This method intended to for use when
     * multiple actions should be done into the same single transaction.
     * @param login login of User that will be received
     * @param connection Connection object on which the query will be executed
     * @return User instance with given login
     * @throws DBException if internal database errors occur
     * @throws InvalidOperationException if User with given login doesn't exist
     */
    private static User getUser(String login, Connection connection)
            throws DBException, InvalidOperationException {
        QueryGetData queryData = new QueryGetData();
        queryData.setFilterFactor("user.login", login);
        List<User> list = getUserList(queryData, connection);
        if (list.isEmpty()) {
            throw new InvalidOperationException(ErrorMessages.NO_SUCH_USER);
        }
        return list.get(0);
    }

    /**
     * Receives single {@link Request} instance from the database
     * on given {@link Connection} object. This method intended to for use when
     * multiple actions should be done into the same single transaction.
     * @param id ID of Request that will be received
     * @param connection Connection object on which the query will be executed
     * @return Request instance with given ID
     * @throws DBException if internal database errors occur
     * @throws InvalidOperationException if Request with given ID doesn't exist
     */
    private static Request getRequest(int id, Connection connection)
            throws DBException, InvalidOperationException {
        QueryGetData queryData = new QueryGetData();
        queryData.setFilterFactor("request.id", String.valueOf(id));
        List<Request> list = getRequestList(queryData, connection);
        if (list.isEmpty()) {
            throw new InvalidOperationException(ErrorMessages.NO_SUCH_REQUEST);
        }
        return list.get(0);
    }

    /**
     * Receives {@link List} of {@link User} entities according to given {@link QueryGetData} object
     * on given {@link Connection} object. This method intended to for use when
     * multiple actions should be done into the same single transaction.
     * @param queryData QueryGetData object that holds the data required to generate desired SQL query
     * @param connection Connection object on which the query will be executed
     * @return List of Users according to given query (can be empty)
     * @throws DBException if given queryData not correct or internal database errors occur
     */
    private static List<User> getUserList(QueryGetData queryData, Connection connection)
            throws DBException {
        return getUserDao(connection).getEntityList(queryData);
    }

    /**
     * Receives {@link List} of {@link Request} entities according to given {@link QueryGetData} object
     * on given {@link Connection} object. This method intended to for use when
     * multiple actions should be done into the same single transaction.
     * @param queryData QueryGetData object that holds the data required to generate desired SQL query
     * @param connection Connection object on which the query will be executed
     * @return List of Requests according to given query (can be empty)
     * @throws DBException if given queryData not correct or internal database errors occur
     */
    private static List<Request> getRequestList(QueryGetData queryData, Connection connection)
            throws DBException {
        return getRequestDao(connection).getEntityList(queryData);
    }

    /**
     * Receives {@link List} of PaymentRecord entities according to given {@link QueryGetData} object.
     * on given {@link Connection} object. This method intended to for use when
     * multiple actions should be done into the same single transaction.
     * @param queryData QueryGetData object that holds the data required to generate desired SQL query
     * @param connection Connection object on which the query will be executed
     * @return List of PaymentRecords according to given query (can be empty)
     * @throws DBException if given queryData not correct or internal database errors occur
     */
    private static List<PaymentRecord> getPaymentRecordList(QueryGetData queryData, Connection connection)
            throws DBException {
        return getPaymentRecordDao(connection).getEntityList(queryData);
    }

    /**
     * Receives {@link Connection} object from DB layer. This method is called when new transaction starts.
     * @return received Connection object
     * @throws DBException if internal database errors occur
     */
    private static Connection startTransaction() throws DBException {
        return DBManager.getConnection();
    }

    /**
     * Commits all changes made during last opened transaction
     * on given {@link Connection} object and releases it.
     * @param connection Connection to be committed and closed
     * @throws DBException if internal database errors occur
     */
    private static void completeTransaction(Connection connection) throws DBException {
        DBManager.commitTransaction(connection);
        DBManager.closeConnection(connection);
    }

    /**
     * Receives new Dao object for {@link PaymentRecord} on given {@link Connection}
     * @param connection Connection on which new Dao will be created
     * @return new PaymentRecord Dao object
     * @throws DBException if DaoFactory was not initialized or given Connection is null
     */
    private static Dao<PaymentRecord> getPaymentRecordDao(Connection connection) throws DBException {
        return DBManager.getDaoFactory().getPaymentRecordDao(connection);
    }

    /**
     * Receives new Dao object for {@link Request} on given {@link Connection}
     * @param connection Connection on which new Dao will be created
     * @return new Request Dao object
     * @throws DBException if DaoFactory was not initialized or given Connection is null
     */
    private static Dao<Request> getRequestDao(Connection connection) throws DBException {
        return DBManager.getDaoFactory().getRequestDao(connection);
    }

    /**
     * Receives new Dao object for {@link User} on given {@link Connection}
     * @param connection Connection on which new Dao will be created
     * @return new User Dao object
     * @throws DBException if DaoFactory was not initialized or given Connection is null
     */
    private static Dao<User> getUserDao(Connection connection) throws DBException {
        return DBManager.getDaoFactory().getUserDao(connection);
    }

}
