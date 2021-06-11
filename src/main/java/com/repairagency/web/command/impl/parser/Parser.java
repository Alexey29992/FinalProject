package com.repairagency.web.command.impl.parser;

import com.repairagency.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Parser {

    private Parser(){
    }

    private static final Logger logger = LogManager.getLogger();

    public static String parseOrder(String sortOrderAttr) {
        String order = Config.DEFAULT_TABLE_ORDER;
        if (sortOrderAttr != null) {
            switch (sortOrderAttr) {
                case "desc":
                    order = "DESC";
                    break;
                case "asc":
                    order = "ASC";
                    break;
                default:
                    logger.trace("Unexpected 'sorting order' parameter");
            }
        }
        return order;
    }

    public static int parseSize(String sizeAttr) {
        int size = Config.DEFAULT_TABLE_SIZE;
        if (sizeAttr != null) {
            switch (sizeAttr) {
                case "5":
                    size = 5;
                    break;
                case "10":
                    size = 10;
                    break;
                case "20":
                    size = 20;
                    break;
                case "40":
                    size = 40;
                    break;
                default:
                    logger.trace("Unexpected 'size' parameter");
            }
        }
        return size;
    }

    public static int parsePage(String pageAttr) {
        int newPage = Config.DEFAULT_TABLE_PAGE;
        if (pageAttr != null) {
            try {
                newPage = Integer.parseInt(pageAttr);
            } catch (NumberFormatException ex) {
                logger.trace("Unexpected 'page' parameter");
            }
        }
        if (newPage < 0) {
            newPage = Config.DEFAULT_TABLE_PAGE;
        }
        return newPage;
    }

    ////////////////////////////////////////////////////////

    public static String parseSort(String sortFactorAttr) {
        String sortFactor = Config.DEFAULT_TABLE_SORT;
        if (sortFactorAttr != null) {
            switch (sortFactorAttr) {
                case "id":
                    sortFactor = "id";
                    break;
                case "creation-date":
                    sortFactor = "creation_date";
                    break;
                case "status":
                    sortFactor = "status_id";
                    break;
                case "price":
                    sortFactor = "price";
                    break;
                case "completion-date":
                    sortFactor = "completion_date";
                    break;
                default:
                    logger.trace("Unexpected 'sorting' parameter");
            }
        }
        return sortFactor;
    }

    public static String parseRequestStatusFilter(String statusFilterAttr) {
        String statusName = null;
        if (statusFilterAttr != null) {
            switch (statusFilterAttr) {
                case "none":
                    break;
                case "new":
                    statusName = "NEW";
                    break;
                case "wait-for-payment":
                    statusName = "WAIT_FOR_PAYMENT";
                    break;
                case "paid":
                    statusName = "PAID";
                    break;
                case "cancelled":
                    statusName = "CANCELLED";
                    break;
                case "in-process":
                    statusName = "IN_PROCESS";
                    break;
                case "done":
                    statusName = "DONE";
                    break;
                default:
                    logger.trace("Unexpected 'filter-status' parameter");
            }
        }
        return statusName;
    }

}
