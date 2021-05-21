package com.my.entities.users;

import com.my.constants.Configuration;
import com.my.exceptions.DBException;
import com.my.exceptions.ExceptionMessages;
import com.my.exceptions.InvalidOperationException;
import com.my.utils.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Admin extends Manager {

    private static final Logger logger = LogManager.getLogger();

    private Admin() throws InvalidOperationException {
        super(Configuration.DEFAULT_ADMIN_LOGIN, Configuration.DEFAULT_ADMIN_PASSWORD, Role.ADMIN);
    }

    private static Admin newAdmin()
            throws InvalidOperationException, DBException {
        logger.debug("Creating instance of Admin");
        Admin admin = new Admin();
        int id = UserManager.addUser(admin);
        admin.setId(id);
        return admin;
    }

    public static Admin getInstance() {
        logger.debug("Receiving instance of Admin");
        try {
            return (Admin) UserManager.getUserList(Role.ADMIN).get(0);
        } catch (DBException ex1) {
            logger.debug("Instance of Admin does not exist in DB");
            try {
                return newAdmin();
            } catch (InvalidOperationException | DBException ex2) {
                logger.fatal("Cannot get instance of Admin");
                throw new IllegalStateException();
            }
        }
    }

    public List<User> getUserList() {
        return UserManager.getUserList();
    }

    public User addUser(String login, String password, Role role)
            throws DBException, InvalidOperationException {
        logger.debug("Adding user with role '{}', login '{}'", role, login);
        switch (role) {
            case MANAGER:
                return Manager.newManager(login, password);
            case MASTER:
                return Master.newMaster(login, password);
            case CLIENT:
                return Client.newClient(login, password);
            default:
                logger.error("Adding user with requested role is not supported");
                throw new InvalidOperationException(ExceptionMessages.ADMIN_CANNOT_ADD_USER_ROLE);
        }
    }

    public void setUserRole(User user, Role role) {
        logger.debug("Applying to User#{} role '{}'", user.getId(), role);
        user.setRole(role);
        user.submitChanges();
    }

    public void removeUser(User user) {
        logger.debug("Removing User#{}", user.getId());
        UserManager.removeUser(user);
    }

}
