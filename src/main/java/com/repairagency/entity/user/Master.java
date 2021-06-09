package com.repairagency.entity.user;

import com.repairagency.entity.EntityManager;
import com.repairagency.entity.Role;
import com.repairagency.entity.User;
import com.repairagency.entity.bean.Request;
import com.repairagency.exception.DBException;
import com.repairagency.exception.InvalidOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Master extends User {

    private static final Logger logger = LogManager.getLogger();

    public Master(String login, String password) {
        super(login, password, Role.MASTER);
    }

    public Master(int id, String login, String password) {
        this(login, password);
        setId(id);
    }

    public void processRequest(Request request)
            throws InvalidOperationException, DBException {
        logger.debug("Master#{} starting to process Request#{}", getId(), request.getId());
        request.setStatus(Request.Status.IN_PROCESS);
        EntityManager.updateRequest(request);
    }

    public void closeRequest(Request request)
            throws InvalidOperationException, DBException {
        logger.debug("Master#{} closing Request#{}", getId(), request.getId());
        request.setStatus(Request.Status.DONE);
        EntityManager.updateRequest(request);
    }

    public List<Request> getRequestList(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        return EntityManager.requestGetAll(chunkSize, chunkNumber, sortingFactor);
    }

}
