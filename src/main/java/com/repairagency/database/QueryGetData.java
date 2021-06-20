package com.repairagency.database;

import java.util.HashMap;
import java.util.Map;

/**
 * Data container that holds data required to create GET query for DAO.
 * Objects of this class intended for use in QueryGetGenerator of any implementation of DAO
 */

public class QueryGetData {

    private final Map<String, String> filterFactors = new HashMap<>();
    private String sortFactor;
    private String sortOrder;
    private int limitFactor;
    private int offsetFactor;

    public void setFilterFactor(String name, String value) {
        filterFactors.put(name, value);
    }

    public Map<String, String> getFilterFactors() {
        return filterFactors;
    }

    public String getSortFactor() {
        return sortFactor;
    }

    public void setSortFactor(String sortFactor) {
        this.sortFactor = sortFactor;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getLimitFactor() {
        return limitFactor;
    }

    public void setLimitFactor(int limitFactor) {
        this.limitFactor = limitFactor;
    }

    public int getOffsetFactor() {
        return offsetFactor;
    }

    public void setOffsetFactor(int offsetFactor) {
        this.offsetFactor = offsetFactor;
    }

}
