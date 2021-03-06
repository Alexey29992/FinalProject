package com.repairagency.database.dao.impl.mysql;

import com.repairagency.database.QueryGetData;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Objects of this class represents generators of GET query strings for MySQL database.
 * Instance of this class should be created with prepared {@link QueryGetData} and basic
 * query string for appropriate DAO. Then desired query can be received by {@link #generateQuery()}.
 * <p>All input of {@link QueryGetData} should be validated to prevent SQL-injections
 */
public class QueryGetGenerator {

    private final QueryGetData data;
    protected String queryBase;
    protected StringBuilder query;

    /**
     * Creates QueryGetGenerator object with given parameters.
     * After creating instance calling {@link #generateQuery()} is expected.
     * @param queryBase base part of SQL query specific to concrete DAO object
     * @param data additional data specific to current SQL query
     */
    public QueryGetGenerator(String queryBase, QueryGetData data) {
        this.queryBase = queryBase;
        this.data = data;
    }

    /**
     * Generates complete SQL query from given in constructor data.
     * Resulted query is ready to be executed.
     * @return SQL query string generated according to given data
     */
    public String generateQuery() {
        query = new StringBuilder(queryBase);
        if (!data.getFilterFactors().isEmpty()) {
            Set<Map.Entry<String, String>> entrySet = data.getFilterFactors().entrySet();
            Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
            Map.Entry<String, String> entry = iterator.next();
            this.where(entry.getKey(), entry.getValue());
            while (iterator.hasNext()) {
                entry = iterator.next();
                this.and(entry.getKey(), entry.getValue());
            }
        }
        if (data.getSortFactor() != null) {
            this.orderBy();
            if (data.getSortOrder() != null) {
                this.orderDirection();
            }
        }
        if (data.getLimitFactor() != 0) {
            this.limit();
            if (data.getOffsetFactor() != 0) {
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
                .append(data.getSortFactor());
    }

    private void orderDirection() {
        query.append(" ")
                .append(data.getSortOrder());
    }

    private void limit() {
        query.append(" LIMIT ")
                .append(data.getLimitFactor());
    }

    private void offset() {
        query.append(" OFFSET ")
                .append(data.getOffsetFactor());
    }

}
