package com.repairagency.database.dao;

import com.repairagency.exceptions.DBException;

public interface Dao<T> {

    int addEntity(T entity) throws DBException;

    void updateEntity(T entity) throws DBException;

    void removeEntity(T entity) throws DBException;

    T getEntityById(int id) throws DBException;

}
