package com.my.entities;

import java.io.Serializable;

public abstract class Item implements PersistentEntity, Serializable {

    protected int id;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

}
