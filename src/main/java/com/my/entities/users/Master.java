package com.my.entities.users;

import com.my.entities.items.Request;
import com.my.exceptions.DBException;
import com.my.exceptions.InvalidOperationException;
import com.my.utils.RequestManager;
import com.my.utils.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Master extends User {

    private static final Logger logger = LogManager.getLogger();

    private Master(String login, String password)
            throws InvalidOperationException {
        super(login, password, Role.MASTER);
    }

    public static User newMaster(String login, String password)
            throws InvalidOperationException, DBException {
        logger.debug("Creating new Master with login '{}'", login);
        Master master = new Master(login, password);
        int id = UserManager.addUser(master);
        master.setId(id);
        return master;
    }

    public void processRequest(Request req) {
        logger.debug("Master#{} starting to process Request#{}", getId(), req.getId());
        req.setStatus(Request.Status.IN_PROCESS);
        req.submitChanges();
    }

    public void closeRequest(Request req) {
        logger.debug("Master#{} closing Request#{}", getId(), req.getId());
        req.setStatus(Request.Status.DONE);
        req.submitChanges();
    }

    public List<Request> getRequestList() {
        return RequestManager.getRequestList(this);
    }

}
