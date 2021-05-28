package com.my.context;

import com.my.database.DBManager;
import com.my.database.dao.mysql.DaoFactoryMysql;
import com.my.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("Initializing context");
        DBManager.setDaoFactory(new DaoFactoryMysql());
        DBManager.init();
        logger.debug("DAOFactory : {}", DBManager.getDaoFactory());
        try {
            logger.debug("Connection : {}", DBManager.getConnection());
        } catch (DBException e) {
            e.printStackTrace();
        }

    }

}
