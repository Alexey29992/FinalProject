package com.repairagency.database.dao;

import com.repairagency.bean.AbstractBean;
import com.repairagency.database.QueryGetData;
import com.repairagency.exception.DBException;

import java.util.List;

/**
 * Data Access Object (DAO) interface. All realizations of DAO should implement it.
 * Contains common methods to work with beans and database (CRUD operations).
 * @param <T> type of bean
 */

public interface Dao<T extends AbstractBean> {

    int addEntity(T entity) throws DBException;

    void updateEntity(T entity) throws DBException;

    void removeEntity(int id) throws DBException;

    List<T> getEntityList(QueryGetData queryData) throws DBException;

}
