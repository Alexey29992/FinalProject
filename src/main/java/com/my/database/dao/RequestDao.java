package com.my.database.dao;

import com.my.entities.items.Request;
import com.my.exceptions.DBException;

import java.util.List;

public interface RequestDao extends Dao<Request> {

    List<Request> getEntityListAll(int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException;

    List<Request> getEntityListByMaster(int masterId, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException;

    List<Request> getEntityListByClient(int clientId, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException;

    List<Request> getEntityListByStatus(Request.Status status, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException;
}
