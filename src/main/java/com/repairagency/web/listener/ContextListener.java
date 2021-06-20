package com.repairagency.web.listener;

import com.repairagency.bean.EntityManager;
import com.repairagency.database.DBManager;
import com.repairagency.exception.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;

/**
 * Context Event Listener that initializes all required instances on Application startup
 */

@WebListener
public class ContextListener implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("Initializing context");
        ServletContext context = sce.getServletContext();
        DBManager.initDataSource();
        DBManager.defaultDaoFactory();
        try {
            context.setAttribute("masterMap", EntityManager.getMasterLogins());
        } catch (DBException ex) {
            logger.fatal("Cannot initialize master list");
            throw new IllegalStateException(ex);
        }
        context.setAttribute("user-balance-updates", new HashMap<Integer, Integer>());
        String defaultLocale = context.getInitParameter("javax.servlet.jsp.jstl.fmt.locale");
        logger.trace("Default locale: {}", defaultLocale);
        context.setAttribute("locale", defaultLocale);
    }

}
