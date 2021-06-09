package com.repairagency.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class QueryGetGenerator {

    public static final String REQUEST_BASE =
            "SELECT request.*, status.status_name FROM request " +
                    "JOIN status ON status_id = status.id";

    private final String queryBase;
    private StringBuilder query;

    private Map<String, String> filterFactors;
    private String sortFactor;
    private boolean descending;
    private int limitFactor;
    private int offsetFactor;

    public QueryGetGenerator(String queryBase) {
        this.queryBase = queryBase;
    }

    public void setFilterFactors(Map<String, String> filterFactors) {
        this.filterFactors = filterFactors;
    }

    public void setSortFactor(String sortFactor) {
        this.sortFactor = sortFactor;
    }

    public void setDescending(boolean descending) {
        this.descending = descending;
    }

    public void setLimitFactor(int limitFactor) {
        this.limitFactor = limitFactor;
    }

    public void setOffsetFactor(int offsetFactor) {
        this.offsetFactor = offsetFactor;
    }

    public String generateQuery() {
        query = new StringBuilder(queryBase);
        if (!filterFactors.isEmpty()) {
            Set<Map.Entry<String, String>> entrySet = filterFactors.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
            Map.Entry<String, String> entry = iterator.next();
            this.where(entry.getKey(), entry.getValue());
            while (iterator.hasNext()) {
                entry = iterator.next();
                this.and(entry.getKey(), entry.getValue());
            }
        }
        if (sortFactor != null) {
            this.orderBy();
            if (descending) {
                this.desc();
            }
        }
        if (limitFactor != 0) {
            this.limit();
            if (offsetFactor != 0) {
                this.offset();
            }
        }
        return query.toString();
    }

    private void where(String column, String value) {
        query.append(" WHERE (")
                .append(column)
                .append(" = '")
                .append(value)
                .append("')");
    }

    private void and(String column, String value) {
        query.append(" AND (")
                .append(column)
                .append(" = '")
                .append(value)
                .append("')");
    }

    private void orderBy() {
        query.append(" ORDER BY ")
                .append(sortFactor);
    }

    private void desc() {
        query.append(" DESC");
    }

    private void limit() {
        query.append(" LIMIT ")
                .append(limitFactor);
    }

    private void offset() {
        query.append(" OFFSET ")
                .append(offsetFactor);
    }

}
