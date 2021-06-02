package com.repairagency.database.dao;

import com.repairagency.entities.beans.PaymentRecord;
import com.repairagency.exceptions.DBException;

import java.util.List;

public interface PaymentRecordDao extends Dao<PaymentRecord> {

    List<PaymentRecord> getEntityList(int parameter, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException;

}
