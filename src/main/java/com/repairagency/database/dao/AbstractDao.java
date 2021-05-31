package com.repairagency.database.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDao {

    private static final Logger logger = LogManager.getLogger();

    protected final Connection connection;

    protected AbstractDao(Connection connection) {
        this.connection = connection;
    }

    protected void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException ex) {
            logger.error("Cannot close the ResultSet", ex);
        }
    }


}
