package com.repairagency.entity.user;

import com.repairagency.entity.EntityManager;
import com.repairagency.entity.Role;
import com.repairagency.entity.User;
import com.repairagency.exception.DBException;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.util.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Admin extends Manager {

    private static final Logger logger = LogManager.getLogger();

    public Admin(int id, String login, String password) {
        super(login, password, Role.ADMIN);
        setId(id);
    }

    public User addUser(String login, String password, Role role)
            throws InvalidOperationException, DBException {
        logger.debug("Adding user with role '{}', login '{}'", role, login);
        Validator.validateLogin(login);
        Validator.validatePassword(password);
        return EntityManager.newUser(login, password, role);
    }

    public void setUserRole(User user, Role role) throws DBException {
        logger.debug("Applying to User#{} role '{}'", user.getId(), role);
        user.setRole(role);
        EntityManager.updateUser(user);
    }

}
