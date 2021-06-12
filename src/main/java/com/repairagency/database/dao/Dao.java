package com.repairagency.database.dao;

import com.repairagency.bean.AbstractBean;
import com.repairagency.database.QueryGetData;
import com.repairagency.exception.DBException;

import java.util.List;

public interface Dao<T extends AbstractBean> {

    int addEntity(T entity) throws DBException;

    void updateEntity(T entity) throws DBException;

    void removeEntity(T entity) throws DBException;

    List<T> getEntityList(QueryGetData queryData) throws DBException;

}
