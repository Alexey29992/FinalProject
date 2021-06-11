package com.repairagency.entity;

import com.repairagency.database.QueryBases;
import com.repairagency.database.QueryGetGenerator;
import com.repairagency.database.wrapper.ManagerRequestData;
import com.repairagency.database.wrapper.QueryData;
import com.repairagency.text.Text;
import com.repairagency.database.DBManager;
import com.repairagency.database.dao.*;
import com.repairagency.entity.bean.PaymentRecord;
import com.repairagency.entity.bean.Request;
import com.repairagency.entity.user.Client;
import com.repairagency.entity.user.Manager;
import com.repairagency.entity.user.Master;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.util.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.List;

public class EntityManager {

    private static final Logger logger = LogManager.getLogger();

    public static List<Request> getClientRequestList(QueryData queryData) throws DBException {
        Connection connection = startTransaction();
        RequestDao dao = getRequestDao(connection);
        QueryGetGenerator generator = new QueryGetGenerator(QueryBases.CLIENT_REQUEST_BASE, queryData);
        String query = generator.generateQuery();
        List<Request> list = dao.getEntityList(query);
        completeTransaction(connection);
        return list;
    }

    public static List<ManagerRequestData> getManagerRequestList(QueryData queryData) throws DBException {
        Connection connection = startTransaction();
        RequestDao dao = getRequestDao(connection);
        QueryGetGenerator generator = new QueryGetGenerator(QueryBases.MANAGER_REQUEST_BASE, queryData);
        String query = generator.generateQuery();
        List<ManagerRequestData> data = dao.getEntityListManager(query);
        completeTransaction(connection);
        return data;
    }

/*    public static List<Request> getMasterRequestList() throws DBException {
        Connection connection = startTransaction();
        RequestDao dao = getRequestDao(connection);
        List<Request> list = dao.getEntityList();
        completeTransaction(connection);
        return list;
    }*/

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
        User user = EntityManager.userGetByLogin(login);
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

    public static Request newRequest(String description, int clientId)
            throws DBException {
        logger.debug("Creating new request from Client#{}", clientId);
        Request request = new Request(description, clientId);
        Connection connection = startTransaction();
        RequestDao dao = getRequestDao(connection);
        int id = dao.addEntity(request);
        request.setId(id);
        completeTransaction(connection);
        return request;
    }

    ////////////////////////////////////////////////////////

    public static PaymentRecord paymentRecordGetById(int id) throws DBException {
        Connection connection = startTransaction();
        PaymentRecordDao dao = getPaymentRecordDao(connection);
        PaymentRecord list = dao.getEntityById(id);
        completeTransaction(connection);
        return list;
    }

    public static List<PaymentRecord> paymentRecordGetByUser(int clientId, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        Connection connection = startTransaction();
        PaymentRecordDao dao = getPaymentRecordDao(connection);
        List<PaymentRecord> list = dao.getEntityList(clientId, chunkSize, chunkNumber, sortingFactor);
        completeTransaction(connection);
        return list;
    }

    ////////////////////////////////////////////////////////

    public static Request getRequest(int id)
            throws DBException {
        Connection connection = startTransaction();
        RequestDao dao = getRequestDao(connection);
        Request req = dao.getEntityById(id);
        completeTransaction(connection);
        return req;
    }

    public static List<Request> requestGetAll(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        Connection connection = startTransaction();
        RequestDao dao = getRequestDao(connection);
        List<Request> list = dao.getEntityListAll(chunkSize, chunkNumber, sortingFactor);
        completeTransaction(connection);
        return list;
    }

    public static List<Request> requestGetByClient(int clientId, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        Connection connection = startTransaction();
        RequestDao dao = getRequestDao(connection);
        List<Request> list = dao.getEntityListByClient(clientId, chunkSize, chunkNumber, sortingFactor);
        completeTransaction(connection);
        return list;
    }

    public static List<Request> requestGetByMaster(int masterId, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        Connection connection = startTransaction();
        RequestDao dao = getRequestDao(connection);
        List<Request> list = dao.getEntityListByMaster(masterId, chunkSize, chunkNumber, sortingFactor);
        completeTransaction(connection);
        return list;
    }

    public static List<Request> requestGetByStatus(Request.Status status, int chunkSize,
                                                   int chunkNumber, String sortingFactor)
            throws DBException {
        Connection connection = startTransaction();
        RequestDao dao = getRequestDao(connection);
        List<Request> list = dao.getEntityListByStatus(status, chunkSize, chunkNumber, sortingFactor);
        completeTransaction(connection);
        return list;
    }

    public static void updateRequest(Request request) throws DBException {
        Connection connection = startTransaction();
        getRequestDao(connection).updateEntity(request);
        completeTransaction(connection);
    }

    ////////////////////////////////////////////////////////

    public static User userGetByLogin(String login) throws DBException {
        Connection connection = startTransaction();
        UserDao userDao = getUserDao(connection);
        User user = userDao.getEntityByLogin(login);
        completeTransaction(connection);
        return user;
    }

    public static User getUserById(int userId) throws DBException {
        Connection connection = startTransaction();
        UserDao dao = getUserDao(connection);
        User user = dao.getEntityById(userId);
        completeTransaction(connection);
        return user;
    }

    public static List<User> usersGetAll(int chunkSize, int chunkNumber, String sortingFactor) throws DBException {
        Connection connection = startTransaction();
        UserDao dao = getUserDao(connection);
        List<User> list = dao.getEntityListAll(chunkSize, chunkNumber, sortingFactor);
        completeTransaction(connection);
        return list;
    }

    public static List<User> usersGetByRole(User.Role role, int chunkSize, int chunkNumber, String sortingFactor) throws DBException {
        Connection connection = startTransaction();
        UserDao dao = getUserDao(connection);
        List<User> list = dao.getEntityListByRole(role, chunkSize, chunkNumber, sortingFactor);
        completeTransaction(connection);
        return list;
    }

    public static void userRemove(User user) throws DBException {
        Connection connection = startTransaction();
        UserDao dao = getUserDao(connection);
        dao.removeEntity(user);
        completeTransaction(connection);
    }

    public static void userAddToDB(User user) throws DBException {
        Connection connection = startTransaction();
        UserDao dao = getUserDao(connection);
        int id = dao.addEntity(user);
        user.setId(id);
        completeTransaction(connection);
    }

    public void userSignOut() {
        // should be realized (deleting Cookies)
    }

    public static void updateUser(User user) throws DBException {
        Connection connection = startTransaction();
        getUserDao(connection).updateEntity(user);
        completeTransaction(connection);
    }

    public static boolean isLoginRegistered(String login) throws DBException {
        Connection connection = startTransaction();
        boolean result = false;
        User user = getUserDao(connection).getEntityByLogin(login);
        if (user != null) {
            result = true;
        }
        completeTransaction(connection);
        return result;
    }

    ////////////////////////////////////////////////////////

    public static int topUpClientBalance(int clientId, int amount) throws DBException {
        logger.debug("Top up Client balance");
        logger.trace("amount#{}$ , Client#{}", amount, clientId);
        Connection connection = startTransaction();
        UserDao userDao = getUserDao(connection);
        logger.trace("Receiving Client from DB");
        Client client = (Client) userDao.getEntityById(clientId);
        PaymentRecordDao paymentRecordDao = getPaymentRecordDao(connection);
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
        UserDao userDao = getUserDao(connection);
        RequestDao requestDao = getRequestDao(connection);
        PaymentRecordDao paymentRecordDao = getPaymentRecordDao(connection);

        logger.trace("Receiving Request instance from DB");
        Request request = requestDao.getEntityById(requestId);

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

    private static Connection startTransaction() throws DBException {
        return DBManager.getConnection();
    }

    private static void completeTransaction(Connection connection) throws DBException {
        DBManager.commitTransaction(connection);
        DBManager.closeConnection(connection);
    }

    private static PaymentRecordDao getPaymentRecordDao(Connection connection) throws DBException {
        return DBManager.getDaoFactory().getPaymentRecordDao(connection);
    }

    private static RequestDao getRequestDao(Connection connection) throws DBException {
        return DBManager.getDaoFactory().getRequestDao(connection);
    }

    private static UserDao getUserDao(Connection connection) throws DBException {
        return DBManager.getDaoFactory().getUserDao(connection);
    }

    private static User createManager(String login, String password)
            throws DBException {
        Connection connection = startTransaction();
        Manager manager = new Manager(login, password);
        UserDao userDao = getUserDao(connection);
        int managerId = userDao.addEntity(manager);
        completeTransaction(connection);
        manager.setId(managerId);
        return manager;
    }

    private static User createMaster(String login, String password)
            throws DBException {
        Connection connection = startTransaction();
        Master master = new Master(login, password);
        UserDao userDao = getUserDao(connection);
        int masterId = userDao.addEntity(master);
        completeTransaction(connection);
        master.setId(masterId);
        return master;
    }

    private static User createClient(String login, String password)
            throws DBException {
        Connection connection = startTransaction();
        Client client = new Client(login, password);
        UserDao userDao = getUserDao(connection);
        int clientId = userDao.addEntity(client);
        completeTransaction(connection);
        client.setId(clientId);
        return client;
    }

}
