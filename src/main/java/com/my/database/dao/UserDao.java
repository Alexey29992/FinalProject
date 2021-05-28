package com.my.database.dao;

import com.my.entities.Role;
import com.my.entities.User;
import com.my.exceptions.DBException;

import java.util.List;

public interface UserDao extends Dao<User> {

    User getEntityByLogin(String login)
            throws DBException;

    List<User> getEntityListAll(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException;

    List<User> getEntityListByRole(Role role, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException;

}
