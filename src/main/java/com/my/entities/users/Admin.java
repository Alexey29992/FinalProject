package com.my.entities.users;

import com.my.config.Config;
import com.my.entities.EntityUtils;
import com.my.entities.Role;
import com.my.entities.User;
import com.my.exceptions.DBException;
import com.my.exceptions.InvalidOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Admin extends Manager {

    private static final Logger logger = LogManager.getLogger();

    private Admin() throws InvalidOperationException {
        super(Config.ADMIN_LOGIN, Config.ADMIN_PASSWORD, Role.ADMIN);
    }

    public Admin(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public User addUser(String login, String password, Role role)
            throws InvalidOperationException, DBException {
        logger.debug("Adding user with role '{}', login '{}'", role, login);
        return EntityUtils.newUser(login, password, role);
    }

    public void setUserRole(User user, Role role) throws DBException {
        logger.debug("Applying to User#{} role '{}'", user.getId(), role);
        user.setRole(role);
        EntityUtils.updateUser(user);
    }

}
