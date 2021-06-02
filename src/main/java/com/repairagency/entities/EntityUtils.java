package com.repairagency.entities;

import com.repairagency.text.Text;
import com.repairagency.database.DBManager;
import com.repairagency.database.dao.*;
import com.repairagency.entities.beans.PaymentRecord;
import com.repairagency.entities.beans.Request;
import com.repairagency.entities.beans.Wallet;
import com.repairagency.entities.users.Client;
import com.repairagency.entities.users.Manager;
import com.repairagency.entities.users.Master;
import com.repairagency.exceptions.DBException;
import com.repairagency.exceptions.ExceptionMessages;
import com.repairagency.exceptions.InvalidOperationException;
import com.repairagency.validators.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.List;

public class EntityUtils {

    private static final Logger logger = LogManager.getLogger();

    ////////////////////////////////////////////////////////

    public static User signUp(String login, String password)
            throws InvalidOperationException, DBException {
        logger.debug("Registering new User with login '{}'", login);
        Validator.validateLogin(login);
        Validator.validatePassword(password);
        if (EntityUtils.isLoginRegistered(login)) {
            logger.debug("Can not register: login is already registered");
            throw new InvalidOperationException(ExceptionMessages.USER_CREATE_LOGIN_REGISTERED);
        }
        return EntityUtils.newUser(login, password, Role.CLIENT);
    }

    public static User signIn(String login, String password)
            throws DBException, InvalidOperationException {
        logger.debug("Entering with login '{}'", login);
        Validator.validateLogin(login);
        Validator.validatePassword(password);
        User user = EntityUtils.userGetByLogin(login);
        if (!user.getPassword().equals(password)) {
            logger.debug("Can not enter: password not correct");
            throw new InvalidOperationException(ExceptionMessages.PASSWORD_INCORRECT);
        }
        return user;
    }

    ////////////////////////////////////////////////////////

    public static User newUser(String login, String password, Role role)
            throws DBException, InvalidOperationException {
        logger.debug("Creating new {} with login '{}'", role, login);
        User user;
        Validator.validateLogin(login);
        Validator.validatePassword(password);
        if (EntityUtils.isLoginRegistered(login)) {
            throw new InvalidOperationException(ExceptionMessages.USER_CREATE_LOGIN_REGISTERED);
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
                throw new InvalidOperationException(ExceptionMessages.USER_CREATE_INVALID_ROLE);
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

    public static List<PaymentRecord> paymentRecordGetByWallet(int walletId, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        Connection connection = startTransaction();
        PaymentRecordDao dao = getPaymentRecordDao(connection);
        List<PaymentRecord> list = dao.getEntityList(walletId, chunkSize, chunkNumber, sortingFactor);
        completeTransaction(connection);
        return list;
    }

    ////////////////////////////////////////////////////////

    public static Request requestGetById(int id)
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
        if (user.getRole() == Role.CLIENT) {
            WalletDao walletDao = getWalletDao(connection);
            Wallet wallet = walletDao.getEntityByUser(user.getId());
            Client client = (Client) user;
            client.setWallet(wallet);
        }
        completeTransaction(connection);
        return user;
    }

    public static User userGetById(int userId) throws DBException {
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

    public static List<User> usersGetByRole(Role role, int chunkSize, int chunkNumber, String sortingFactor) throws DBException {
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
        boolean result = getUserDao(connection).isLoginRegistered(login);
        completeTransaction(connection);
        return result;
    }

    ////////////////////////////////////////////////////////

    public static void walletAddMoney(Wallet wallet, int amount) throws DBException {
        logger.debug("Adding {}$ to Wallet#{}", amount, wallet.getId());
        Connection connection = startTransaction();
        PaymentRecordDao paymentRecordDao = getPaymentRecordDao(connection);
        WalletDao walletDao = getWalletDao(connection);
        logger.debug("Creating new PaymentRecord for Wallet#{}", wallet.getId());
        PaymentRecord paymentRecord = new PaymentRecord(amount, wallet.getId(), Text.PAYMENT_RECORD_ADD_MONEY);
        paymentRecordDao.addEntity(paymentRecord);
        walletDao.updateEntity(wallet);
        completeTransaction(connection);
        int newBalance = wallet.getBalance() + amount;
        wallet.setBalance(newBalance);
    }

    public static void walletMakePayment(Wallet wallet, Request request)
            throws InvalidOperationException, DBException {
        int amount = request.getPrice();
        logger.debug("Taking {}$ from Wallet#{}", amount, wallet.getId());
        Validator.validatePayment(wallet.getBalance(), amount, request);
        Connection connection = startTransaction();
        PaymentRecordDao paymentRecordDao = getPaymentRecordDao(connection);
        logger.debug("Creating new PaymentRecord for Wallet#{}", wallet.getId());
        PaymentRecord paymentRecord = new PaymentRecord(amount, wallet.getId(),
                Text.PAYMENT_RECORD_PAY_MONEY + request.getId());
        paymentRecordDao.addEntity(paymentRecord);
        WalletDao walletDao = getWalletDao(connection);
        walletDao.updateEntity(wallet);
        completeTransaction(connection);
        int newBalance = wallet.getBalance() - amount;
        wallet.setBalance(newBalance);
    }

    public static Wallet walletGetById(int walletId) throws DBException {
        Connection connection = startTransaction();
        WalletDao dao = getWalletDao(startTransaction());
        Wallet wallet = dao.getEntityById(walletId);
        completeTransaction(connection);
        return wallet;
    }

    public static Wallet walletGetByUser(int userId) throws DBException {
        Connection connection = startTransaction();
        WalletDao dao = getWalletDao(connection);
        Wallet wallet = dao.getEntityByUser(userId);
        completeTransaction(connection);
        return wallet;
    }

    public static void updateWallet(Wallet wallet) throws DBException {
        Connection connection = startTransaction();
        getWalletDao(connection).updateEntity(wallet);
        completeTransaction(connection);
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

    private static WalletDao getWalletDao(Connection connection) throws DBException {
        return DBManager.getDaoFactory().getWalletDao(connection);
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
        Wallet wallet = new Wallet(clientId);
        WalletDao walletDao = getWalletDao(connection);
        walletDao.addEntity(wallet);
        completeTransaction(connection);
        client.setId(clientId);
        return client;
    }

}
