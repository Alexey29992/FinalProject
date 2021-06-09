package com.repairagency.database.wrapper;

import java.util.HashMap;
import java.util.Map;

public class QueryData {

    private final Map<String, String> filterFactors = new HashMap<>();
    private String sortFactor;
    private boolean descending;
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

    public boolean isDescending() {
        return descending;
    }

    public void setDescending(boolean descending) {
        this.descending = descending;
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
