package com.repairagency.database.dao;

import com.repairagency.database.wrapper.ManagerRequestData;
import com.repairagency.entity.bean.Request;
import com.repairagency.exception.DBException;

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

    List<Request> getEntityList(String query) throws DBException;

    List<ManagerRequestData> getEntityListManager(String query) throws DBException;

}
