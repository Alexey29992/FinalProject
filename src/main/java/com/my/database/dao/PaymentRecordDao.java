package com.my.database.dao;

import com.my.entities.items.PaymentRecord;
import com.my.exceptions.DBException;

import java.util.List;

public interface PaymentRecordDao extends Dao<PaymentRecord> {

    List<PaymentRecord> getEntityList(int parameter, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException;

}
