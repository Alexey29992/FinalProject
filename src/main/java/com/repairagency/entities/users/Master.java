package com.repairagency.entities.users;

import com.repairagency.entities.EntityUtils;
import com.repairagency.entities.Role;
import com.repairagency.entities.User;
import com.repairagency.entities.beans.Request;
import com.repairagency.exceptions.DBException;
import com.repairagency.exceptions.InvalidOperationException;
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
        EntityUtils.updateRequest(request);
    }

    public void closeRequest(Request request)
            throws InvalidOperationException, DBException {
        logger.debug("Master#{} closing Request#{}", getId(), request.getId());
        request.setStatus(Request.Status.DONE);
        EntityUtils.updateRequest(request);
    }

    public List<Request> getRequestList(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException {
        return EntityUtils.requestGetAll(chunkSize, chunkNumber, sortingFactor);
    }

}
