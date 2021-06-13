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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityManager {

    private static final Logger logger = LogManager.getLogger();

    private EntityManager() {
    }

    ////////////////////////////////////////////////////////

    public static User signUp(String login, String password)
            throws InvalidOperationException, DBException {
        logger.debug("Registering new User with login '{}'", login);
        if (EntityManager.isLoginRegistered(login)) {
            logger.debug("Can not register: login is already registered");
            throw new InvalidOperationException(ErrorMessages.USER_CREATE_LOGIN_REGISTERED);
        }
        return EntityManager.newUser(login, password, User.Role.CLIENT);
    }

    public static User signIn(String login, String password)
            throws DBException, InvalidOperationException {
        logger.debug("Entering with login '{}'", login);
        User user = EntityManager.getUser(login);
        if (user == null) {
            logger.debug("Can not enter: '{}' not registered", login);
            throw new InvalidOperationException(ErrorMessages.USER_LOGIN_NOT_FOUND);
        }
        if (!user.getPassword().equals(password)) {
            logger.debug("Can not enter: password not correct");
            throw new InvalidOperationException(ErrorMessages.USER_PASSWORD_INCORRECT);
        }
        return user;
    }

    ////////////////////////////////////////////////////////

    public static User newUser(String login, String password, User.Role role)
            throws DBException, InvalidOperationException {
        logger.debug("Creating new {} with login '{}'", role, login);
        User user;
        Validator.validateLogin(login);
        Validator.validatePassword(password);
        if (EntityManager.isLoginRegistered(login)) {
            throw new InvalidOperationException(ErrorMessages.USER_CREATE_LOGIN_REGISTERED);
        }
        switch (role) {
            case CLIENT:
                user = createClient(login, password);
                break;
            case MASTER:
                user = createMaster(login, password);
                break;
            case MANAGER:
                user = createManager(login, password);
                break;
            default:
                logger.error("User role not valid");
                throw new InvalidOperationException(ErrorMessages.USER_CREATE_INVALID_ROLE);
        }
        logger.trace("Successfully created {} with id = {}", role, user.getId());
        return user;
    }

    ////////////////////////////////////////////////////////

    public static List<PaymentRecord> getPaymentRecordList(QueryGetData queryData) throws DBException {
        Connection connection = startTransaction();
        List<PaymentRecord> requests = getPaymentRecordList(queryData, connection);
        completeTransaction(connection);
        return requests;
    }

    ////////////////////////////////////////////////////////

    public static void newRequest(String description, int clientId) throws DBException {
        logger.debug("Creating new request from Client#{}", clientId);
        Request request = new Request(description, clientId);
        Connection connection = startTransaction();
        Dao<Request> dao = getRequestDao(connection);
        int id = dao.addEntity(request);
        request.setId(id);
        completeTransaction(connection);
    }

    public static Request getRequest(int id)
            throws DBException, InvalidOperationException {
        Connection connection = startTransaction();
        Request request = getRequest(id, connection);
        completeTransaction(connection);
        return request;
    }

    public static List<Request> getRequestList(QueryGetData queryData) throws DBException {
        Connection connection = startTransaction();
        List<Request> requests = getRequestList(queryData, connection);
        completeTransaction(connection);
        return requests;
    }

    public static void updateRequest(Request request) throws DBException {
        Connection connection = startTransaction();
        getRequestDao(connection).updateEntity(request);
        completeTransaction(connection);
    }

    ////////////////////////////////////////////////////////

    public static User getUser(int id)
            throws DBException, InvalidOperationException {
        Connection connection = startTransaction();
        User user = getUser(id, connection);
        completeTransaction(connection);
        return user;
    }

    public static User getUser(String login)
            throws DBException, InvalidOperationException {
        Connection connection = startTransaction();
        User user = getUser(login, connection);
        completeTransaction(connection);
        return user;
    }

    public static List<User> getUserList(QueryGetData queryData) throws DBException {
        Connection connection = startTransaction();
        List<User> users = getUserList(queryData, connection);
        completeTransaction(connection);
        return users;
    }

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

    public static void updateUser(User request) throws DBException {
        Connection connection = startTransaction();
        getUserDao(connection).updateEntity(request);
        completeTransaction(connection);
    }

    public static void removeUser(User user) throws DBException {
        Connection connection = startTransaction();
        Dao<User> dao = getUserDao(connection);
        dao.removeEntity(user);
        completeTransaction(connection);
    }

    public static boolean isLoginRegistered(String login) throws DBException {
        boolean result = true;
        try {
            getUser(login);
            logger.trace("Login {} is free", login);
        } catch (InvalidOperationException ex) {
            logger.trace("Login {} is registered", login);
            result = false;
        }
        return result;
    }

    ////////////////////////////////////////////////////////

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

    public static int makePayment(Client client, int requestId)
            throws InvalidOperationException, DBException {
        logger.debug("Processing payment");
        logger.trace("Client#{} , Request#{}", client.getId(), requestId);
        Connection connection = startTransaction();
        Dao<User> userDao = getUserDao(connection);
        Dao<Request> requestDao = getRequestDao(connection);
        Dao<PaymentRecord> paymentRecordDao = getPaymentRecordDao(connection);

        logger.trace("Receiving Request instance from DB");
        Request request = getRequest(requestId, connection);

        int amount = request.getPrice();

        logger.trace("Validating payment balance : {}, price {}", client.getBalance(), request.getPrice());
        Validator.validatePayment(client, request, amount);

        logger.trace("Updating Request");
        request.setStatus(Request.Status.PAID);
        requestDao.updateEntity(request);

        logger.trace("Creating new PaymentRecord for Client");
        PaymentRecord paymentRecord = new PaymentRecord(amount, client.getId(),
                Text.PAYMENT_RECORD_PAY_MONEY + request.getId());
        paymentRecordDao.addEntity(paymentRecord);

        int newBalance = client.getBalance() - amount;
        logger.trace("Updating Client in DB");
        client.setBalance(newBalance);
        userDao.updateEntity(client);

        completeTransaction(connection);
        return newBalance;
    }

    ////////////////////////////////////////////////////////

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

    private static List<User> getUserList(QueryGetData queryData, Connection connection)
            throws DBException {
        return getUserDao(connection).getEntityList(queryData);
    }

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

    private static List<Request> getRequestList(QueryGetData queryData, Connection connection)
            throws DBException {
        return getRequestDao(connection).getEntityList(queryData);
    }

    private static List<PaymentRecord> getPaymentRecordList(QueryGetData queryData, Connection connection)
            throws DBException {
        return getPaymentRecordDao(connection).getEntityList(queryData);
    }

    private static Connection startTransaction() throws DBException {
        return DBManager.getConnection();
    }

    private static void completeTransaction(Connection connection) throws DBException {
        DBManager.commitTransaction(connection);
        DBManager.closeConnection(connection);
    }

    private static Dao<PaymentRecord> getPaymentRecordDao(Connection connection) throws DBException {
        return DBManager.getDaoFactory().getPaymentRecordDao(connection);
    }

    private static Dao<Request> getRequestDao(Connection connection) throws DBException {
        return DBManager.getDaoFactory().getRequestDao(connection);
    }

    private static Dao<User> getUserDao(Connection connection) throws DBException {
        return DBManager.getDaoFactory().getUserDao(connection);
    }

    private static User createManager(String login, String password)
            throws DBException {
        Connection connection = startTransaction();
        Manager manager = new Manager(login, password);
        Dao<User> userDao = getUserDao(connection);
        int managerId = userDao.addEntity(manager);
        completeTransaction(connection);
        manager.setId(managerId);
        return manager;
    }

    private static User createMaster(String login, String password)
            throws DBException {
        Connection connection = startTransaction();
        Master master = new Master(login, password);
        Dao<User> userDao = getUserDao(connection);
        int masterId = userDao.addEntity(master);
        completeTransaction(connection);
        master.setId(masterId);
        return master;
    }

    private static User createClient(String login, String password)
            throws DBException {
        Connection connection = startTransaction();
        Client client = new Client(login, password);
        Dao<User> userDao = getUserDao(connection);
        int clientId = userDao.addEntity(client);
        completeTransaction(connection);
        client.setId(clientId);
        return client;
    }

}
