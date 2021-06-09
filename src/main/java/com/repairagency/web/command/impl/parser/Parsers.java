package com.repairagency.web.command.impl.parser;

import com.repairagency.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Parsers {

    private Parsers(){
    }

    private static final Logger logger = LogManager.getLogger();

    public static String parseStatusFilter(String statusFilterAttr) {
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

    public static String parseSort(String sortFactorAttr) {
        String sortFactor = null;
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

    public static boolean parseOrder(String sortOrderAttr) {
        boolean descending = false;
        if (sortOrderAttr != null) {
            if ("desc".equals(sortOrderAttr)) {
                descending = true;
            } else {
                logger.trace("Unexpected 'sorting order' parameter");
            }
        }
        return descending;
    }

    public static int parseSize(String sizeAttr) {
        int size = Config.ROWS_PER_PAGE;
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
        int newPage = 0;
        if (pageAttr != null) {
            try {
                newPage = Integer.parseInt(pageAttr);
            } catch (NumberFormatException ex) {
                logger.trace("Unexpected 'page' parameter");
            }
        }
        if (newPage < 0) {
            newPage = 0;
        }
        return newPage;
    }

}
