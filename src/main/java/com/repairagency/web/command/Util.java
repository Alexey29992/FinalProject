package com.repairagency.web.command;

import com.repairagency.bean.User;
import com.repairagency.bean.data.PaymentRecord;
import com.repairagency.bean.data.Request;
import com.repairagency.config.Config;
import com.repairagency.database.DBFields;
import com.repairagency.database.QueryGetData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
     * Parses sort column for table of {@link Request}s
     * @param sortFactorAttr string that contains sort attribute
     * @return parsed sort column or default one if parsing failed
     */
    public static String parseSortRequest(String sortFactorAttr) {
        String sortFactor = Config.DEFAULT_TABLE_SORT;
        if (sortFactorAttr != null) {
            switch (sortFactorAttr) {
                case "id":
                    sortFactor = DBFields.ID;
                    break;
                case "creation-date":
                    sortFactor = DBFields.REQUEST_CREATION_DATE;
                    break;
                case "status":
                    sortFactor = DBFields.REQUEST_STATUS_ID;
                    break;
                case "price":
                    sortFactor = DBFields.REQUEST_PRICE;
                    break;
                case "completion-date":
                    sortFactor = DBFields.REQUEST_COMPLETION_DATE;
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
                    sortFactor = DBFields.ID;
                    break;
                case "login":
                    sortFactor = DBFields.USER_LOGIN;
                    break;
                case "role":
                    sortFactor = DBFields.USER_ROLE_ID;
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
                    sortFactor = DBFields.ID;
                    break;
                case "date":
                    sortFactor = DBFields.PR_DATE;
                    break;
                case "sum":
                    sortFactor = DBFields.PR_SUM;
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
                    statusName = Request.Status.NEW.name();
                    break;
                case "wait-for-payment":
                    statusName = Request.Status.WAIT_FOR_PAYMENT.name();
                    break;
                case "paid":
                    statusName = Request.Status.PAID.name();
                    break;
                case "cancelled":
                    statusName = Request.Status.CANCELLED.name();
                    break;
                case "in-process":
                    statusName = Request.Status.IN_PROCESS.name();
                    break;
                case "done":
                    statusName = Request.Status.DONE.name();
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
                    statusName = Request.Status.NEW.name();
                    break;
                case "wait-for-payment":
                    statusName = Request.Status.WAIT_FOR_PAYMENT.name();
                    break;
                case "paid":
                    statusName = Request.Status.PAID.name();
                    break;
                case "cancelled":
                    statusName = Request.Status.CANCELLED.name();
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
                    statusName = Request.Status.IN_PROCESS.name();
                    break;
                case "done":
                    statusName = Request.Status.DONE.name();
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
                    userRole = User.Role.MASTER.name();
                    break;
                case "client":
                    userRole = User.Role.CLIENT.name();
                    break;
                case "manager":
                    userRole = User.Role.MANAGER.name();
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

    /**
     * Parses common for all tables parameters, such as sort order, page size and
     * page number. Internally sets chunk size of data in queryData parameter as
     * [real_size + 1] to allow {@link #setPageParams(HttpServletRequest, List, int[])}
     * determine whether this is the last page of table or not.
     * @param queryData {@link QueryGetData} object in which parsed parameters will be written
     * @param req request that contains table parameters
     * @return array of two ints: int[0] - parsed size, int[1] - parsed page.
     * This array is used in {@link #setPageParams(HttpServletRequest, List, int[])}
     */
    public static int[] parseTableParams(QueryGetData queryData, HttpServletRequest req) {
        String sortOrderAttr = req.getParameter("sort-order");
        logger.trace("sort-order : {}", sortOrderAttr);
        String sizeAttr = req.getParameter("size");
        logger.trace("size : {}", sizeAttr);
        String pageAttr = req.getParameter("page");
        logger.trace("page : {}", pageAttr);

        String sortOrder = Util.parseOrder(sortOrderAttr);
        int size = Util.parseSize(sizeAttr);
        int page = Util.parsePage(pageAttr);

        queryData.setSortOrder(sortOrder);
        queryData.setLimitFactor(size + 1);
        queryData.setOffsetFactor(page * size);
        int[] result = new int[2];
        result[0] = size;
        result[1] = page;
        return result;
    }

    /**
     * Bounds table page parameters (page, hasNextPage, hasPrevPage)
     * as attributes to {@link HttpServletRequest} object.
     * Internally removes last member of input data list if it exists on [size + 1]
     * position as it is needed only to determine presence of the next page.
     * @param req request object to set table parameters
     * @param data {@link List} of data that will be displayed at the page
     * @param pageData array of two ints:
     *                 int[0] - page size parameter,
     *                 int[1] - page number parameter
     */
    public static void setPageParams(HttpServletRequest req, List<?> data, int[] pageData) {
        boolean hasNextPage = false;
        boolean hasPrevPage = false;
        int size = pageData[0];
        int page = pageData[1];
        if (data.size() == size + 1) {
            data.remove(size);
            hasNextPage = true;
        }
        if (page > 0) {
            hasPrevPage = true;
        }
        req.setAttribute("page", page);
        req.setAttribute("hasNextPage", hasNextPage);
        req.setAttribute("hasPrevPage", hasPrevPage);
    }

    /**
     * Parses table sort order.
     * @param sortOrderAttr string that contains order attribute
     * @return parsed sort order or default one if parsing failed
     */
    private static String parseOrder(String sortOrderAttr) {
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
    private static int parseSize(String sizeAttr) {
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
    private static int parsePage(String pageAttr) {
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

}
