package com.repairagency.database.dao;

import com.repairagency.entities.items.Wallet;
import com.repairagency.exceptions.DBException;

public interface WalletDao extends Dao<Wallet> {

    Wallet getEntityByUser(int userId) throws DBException;

}
