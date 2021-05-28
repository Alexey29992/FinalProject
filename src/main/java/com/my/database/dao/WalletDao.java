package com.my.database.dao;

import com.my.entities.items.Wallet;
import com.my.exceptions.DBException;

public interface WalletDao extends Dao<Wallet> {

    Wallet getEntityByUser(int userId) throws DBException;

}
