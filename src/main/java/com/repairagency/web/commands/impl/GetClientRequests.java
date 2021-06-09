package com.repairagency.web.commands.impl;

import com.repairagency.PagePath;
import com.repairagency.config.Config;
import com.repairagency.utils.QueryGetGenerator;
import com.repairagency.entities.EntityManager;
import com.repairagency.entities.User;
import com.repairagency.entities.beans.Request;
import com.repairagency.exceptions.DBException;
import com.repairagency.web.commands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetClientRequests implements Command {

    private static final Logger logger = LogManager.getLogger();
    private int newPage;
    private int size;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : get-client-requests");
        HttpSession session = req.getSession();
        QueryGetGenerator generator = parseHttpParams(req);
        String query = generator.generateQuery();
        List<Request> requestList;
        try {
            requestList = EntityManager.getRequestList(query);
        } catch (DBException ex) {
            logger.error("Cannot get Request List", ex);
            session.setAttribute("error", ex.getPublicMessage());
            return PagePath.ERROR;
        }
        req.setAttribute("page", newPage);
        boolean hasNextPage = false;
        boolean hasPrevPage = false;
        if (requestList.size() == size + 1) {
            requestList.remove(size - 1);
            hasNextPage = true;
        }
        if (newPage > 0) {
            hasPrevPage = true;
        }
        req.setAttribute("requestList", requestList);
        req.setAttribute("hasNextPage", hasNextPage);
        req.setAttribute("hasPrevPage", hasPrevPage);
        req.setAttribute("forwarded", 1);
        return PagePath.CLIENT_REQUESTS;
    }

    QueryGetGenerator parseHttpParams(HttpServletRequest req) {
        logger.trace("Parsing HTTP parameters for Request query");
        String statusFilterAttr = req.getParameter("filter-status");
        logger.trace("statusFilter : {}", statusFilterAttr);
        String sortFactorAttr = req.getParameter("sort-factor");
        logger.trace("sortingFactor : {}", sortFactorAttr);
        String sortOrderAttr = req.getParameter("sort-order");
        logger.trace("sortOrder : {}", sortOrderAttr);
        String sizeAttr = req.getParameter("size");
        logger.trace("size : {}", sizeAttr);
        String pageAttr = req.getParameter("page");
        logger.trace("page : {}", pageAttr);
        User user = (User) req.getSession().getAttribute("user");
        logger.trace("client : {}", user);
        Map<String, String> filterFactors = new HashMap<>();
        filterFactors.put("client_id", String.valueOf(user.getId()));
        String column = "status_name";
        if (statusFilterAttr != null) {
            switch (statusFilterAttr) {
                case "none":
                    break;
                case "new":
                    filterFactors.put(column, "NEW");
                    break;
                case "wait-for-payment":
                    filterFactors.put(column, "WAIT_FOR_PAYMENT");
                    break;
                case "paid":
                    filterFactors.put(column, "PAID");
                    break;
                case "cancelled":
                    filterFactors.put(column, "CANCELLED");
                    break;
                case "in-process":
                    filterFactors.put(column, "IN_PROCESS");
                    break;
                case "done":
                    filterFactors.put(column, "DONE");
                    break;
                default:
                    logger.trace("Unexpected 'filter-status' parameter");
            }
        }
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
        boolean descending = false;
        if (sortOrderAttr != null) {
            switch (sortOrderAttr) {
                case "desc":
                    descending = true;
                    break;
                case "asc":
                    descending = false;
                    break;
                default:
                    logger.trace("Unexpected 'sorting order' parameter");
            }
        }
        size = Config.ROWS_PER_PAGE;
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
        QueryGetGenerator generator = new QueryGetGenerator(QueryGetGenerator.REQUEST_BASE);
        generator.setDescending(descending);
        generator.setSortFactor(sortFactor);
        generator.setFilterFactors(filterFactors);
        generator.setLimitFactor(size + 1);
        generator.setOffsetFactor(newPage * size);
        return generator;
    }

}
