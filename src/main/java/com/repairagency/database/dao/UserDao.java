package com.repairagency.database.dao;

import com.repairagency.entity.Role;
import com.repairagency.entity.User;
import com.repairagency.exception.DBException;

import java.util.List;

public interface UserDao extends Dao<User> {

    User getEntityByLogin(String login)
            throws DBException;

    List<User> getEntityListAll(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException;

    List<User> getEntityListByRole(Role role, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException;

}
