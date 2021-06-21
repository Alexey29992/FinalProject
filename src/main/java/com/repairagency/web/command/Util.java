package com.repairagency.web.command;

import com.repairagency.bean.User;
import com.repairagency.bean.data.PaymentRecord;
import com.repairagency.bean.data.Request;
import com.repairagency.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class that contains some common methods to use in
 * different commands (for example URL parameter parsers)
 */
public class Util {

    private Util(){
    }

    private static final Logger logger = LogManager.getLogger();

    /**
     * Parses table sort order.
     * @param sortOrderAttr string that contains order attribute
     * @return parsed sort order or default one if parsing failed
     */
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

    private static final Set<String> sizes = new HashSet<>(Arrays.asList("5", "10", "20", "40"));

    /**
     * Parses table page size.
     * @param sizeAttr string that contains size attribute
     * @return parsed table size or default one if parsing failed
     */
    public static int parseSize(String sizeAttr) {
        int size = Config.DEFAULT_TABLE_SIZE;
        if (sizes.contains(sizeAttr)) {
            size = Integer.parseInt(sizeAttr);
        } else {
            logger.trace("Unexpected 'size' parameter");
        }
        return size;
    }

    /**
     * Parses table page number
     * @param pageAttr string that contains page attribute
     * @return parsed page or default one if parsing failed
     */
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

    /**
     * Parses sort column for table of {@link Request}s
     * @param sortFactorAttr string that contains sort attribute
     * @return parsed sort column or default one if parsing failed
     */
    public static String parseSortRequest(String sortFactorAttr) {
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
                    logger.trace("Unexpected request sort parameter");
            }
        }
        return sortFactor;
    }

    /**
     * Parses sort column for table of {@link User}s
     * @param sortFactorAttr string that contains sort attribute
     * @return parsed sort column or default one if parsing failed
     */
    public static String parseSortUser(String sortFactorAttr) {
        String sortFactor = Config.DEFAULT_TABLE_SORT;
        if (sortFactorAttr != null) {
            switch (sortFactorAttr) {
                case "id":
                    sortFactor = "id";
                    break;
                case "login":
                    sortFactor = "login";
                    break;
                case "role":
                    sortFactor = "role_id";
                    break;
                default:
                    logger.trace("Unexpected user sort parameter");
            }
        }
        return sortFactor;
    }

    /**
     * Parses sort column for table of {@link PaymentRecord}s
     * @param sortFactorAttr string that contains sort attribute
     * @return parsed sort column or default one if parsing failed
     */
    public static String parseSortPaymentRecord(String sortFactorAttr) {
        String sortFactor = Config.DEFAULT_TABLE_SORT;
        if (sortFactorAttr != null) {
            switch (sortFactorAttr) {
                case "id":
                    sortFactor = "id";
                    break;
                case "date":
                    sortFactor = "date";
                    break;
                case "sum":
                    sortFactor = "sum";
                    break;
                default:
                    logger.trace("Unexpected payment record sort parameter");
            }
        }
        return sortFactor;
    }

    /**
     * Parses {@link Request.Status} name
     * @param statusAttr string that contains status attribute
     * @return  parsed status name or null if parsing failed
     */
    public static String parseStatus(String statusAttr) {
        String statusName = null;
        if (statusAttr != null) {
            switch (statusAttr) {
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
                    logger.trace("Unexpected status parameter");
            }
        }
        return statusName;
    }

    /**
     * Parses {@link Request.Status} name available on Manager's page
     * @param statusAttr string that contains status attribute
     * @return  parsed status name or null if parsing failed
     */
    public static String parseStatusManager(String statusAttr) {
        String statusName = null;
        if (statusAttr != null) {
            switch (statusAttr) {
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
                default:
                    logger.trace("Unexpected manager status parameter");
            }
        }
        return statusName;
    }

    /**
     * Parses {@link Request.Status} name available on Master's page
     * @param statusAttr string that contains status attribute
     * @return  parsed status name or null if parsing failed
     */
    public static String parseStatusMaster(String statusAttr) {
        String statusName = null;
        if (statusAttr != null) {
            switch (statusAttr) {
                case "none":
                    break;
                case "in-process":
                    statusName = "IN_PROCESS";
                    break;
                case "done":
                    statusName = "DONE";
                    break;
                default:
                    logger.trace("Unexpected master status parameter");
            }
        }
        return statusName;
    }

    /**
     * Parses {@link User.Role} name
     * @param userRoleAttr string that contains role attribute
     * @return parsed role name or null if parsing failed
     */
    public static String parseUserRole(String userRoleAttr) {
        String userRole = null;
        if (userRoleAttr != null) {
            switch(userRoleAttr) {
                case "none":
                    break;
                case "master":
                    userRole = "MASTER";
                    break;
                case "client":
                    userRole = "CLIENT";
                    break;
                case "manager":
                    userRole = "MANAGER";
                    break;
                default:
                    logger.trace("Unexpected user role parameter");
            }
        }
        return userRole;
    }

    /**
     * Receives address of JSP page that depends on {@link User.Role} according to {@link PagePath}
     * @param role Role of User address of which page will be returned
     * @param prefix prefix of returned address
     * @param suffix suffix of returned address
     * @return address of JSP page according to given role and PagePath
     */
    public static String getRoleDependentAddress(User.Role role, String prefix, String suffix) {
        StringBuilder address = new StringBuilder(prefix);
        switch (role) {
            case CLIENT:
                address.append(PagePath.CLIENT);
                break;
            case MANAGER:
            case ADMIN:
                address.append(PagePath.MANAGER);
                break;
            case MASTER:
                address.append(PagePath.MASTER);
                break;
            default:
                logger.error("Cannot determine path. Invalid role given.");
                return address.append(PagePath.ERROR).toString();
        }
        return address.append(suffix).toString();
    }

}
