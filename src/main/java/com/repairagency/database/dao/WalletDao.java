package com.repairagency.database.dao;

import com.repairagency.entities.beans.Wallet;
import com.repairagency.exceptions.DBException;

public interface WalletDao extends Dao<Wallet> {

    Wallet getEntityByUser(int userId) throws DBException;

}
