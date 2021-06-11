package com.repairagency.web.context;

import com.repairagency.database.DBManager;
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
        sce.getServletContext().setAttribute("user-balance-updates", new HashMap<Integer, Integer>());
        DBManager.initDataSource();
        DBManager.defaultDaoFactory();
    }

}
