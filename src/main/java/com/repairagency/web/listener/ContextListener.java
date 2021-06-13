package com.repairagency.web.listener;

import com.repairagency.bean.EntityManager;
import com.repairagency.database.DBManager;
import com.repairagency.exception.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;

@WebListener
public class ContextListener implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("Initializing context");
        DBManager.initDataSource();
        DBManager.defaultDaoFactory();
        sce.getServletContext().setAttribute("user-balance-updates", new HashMap<Integer, Integer>());
        try {
            sce.getServletContext().setAttribute("masterList", EntityManager.getMasterLogins());
        } catch (DBException ex) {
            logger.fatal("Cannot initialize master list");
            throw new IllegalStateException(ex);
        }
    }

}
