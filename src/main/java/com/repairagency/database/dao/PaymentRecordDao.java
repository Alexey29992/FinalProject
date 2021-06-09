package com.repairagency.database.dao;

import com.repairagency.entity.bean.PaymentRecord;
import com.repairagency.exception.DBException;

import java.util.List;

public interface PaymentRecordDao extends Dao<PaymentRecord> {

    List<PaymentRecord> getEntityList(int parameter, int chunkSize, int chunkNumber, String sortingFactor)
            throws DBException;

}
