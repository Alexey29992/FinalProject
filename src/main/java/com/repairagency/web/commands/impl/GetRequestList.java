package com.repairagency.web.commands.impl;

import com.repairagency.PagePath;
import com.repairagency.config.Config;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetRequestList implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : get-request-list");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String statusFilterAttr = req.getParameter("filter-status");
        logger.trace("statusFilter : {}", statusFilterAttr);
        String sortFactorAttr = req.getParameter("sorting-factor");
        logger.trace("sortingFactor : {}", sortFactorAttr);
        String sortOrderAttr = req.getParameter("sorting-order");
        logger.trace("sortOrder : {}", sortOrderAttr);
        String nextPageAttr = req.getParameter("next-page");
        logger.trace("nextPage : {}", nextPageAttr);
        String rowsAttr = req.getParameter("rows-per-page");
        logger.trace("rows-per-page : {}", rowsAttr);
        Integer currentPageAttr = (Integer) req.getAttribute("currentPage");
        logger.trace("currentPage : {}", currentPageAttr);

        int chunkSize = Config.ROWS_PER_PAGE;
        int page = 0;
        Request.Status filterFactor = null;
        try {
            chunkSize = Integer.parseInt(rowsAttr);
            page = currentPageAttr != null ? currentPageAttr : 0;
            logger.trace("page : {}", page);
        } catch (NumberFormatException ex) {
            logger.trace("Unknown 'rows-per-page' parameter value");
        }
        try {
            if ("first".equals(nextPageAttr)) {
                page = 0;
            } else {
                page += Integer.parseInt(nextPageAttr);
            }
        } catch (NumberFormatException ex) {
            logger.trace("Unknown 'next-page' parameter value");
        }

        try {
            if (!"NONE".equals(statusFilterAttr)) {
                filterFactor = Request.Status.valueOf(statusFilterAttr);
            }
        } catch (IllegalArgumentException ex) {
            logger.trace("Unknown 'filter-factor' parameter value");
        }



        if (!isSortingFactorValid(sortFactorAttr)) {
            logger.trace("Unknown 'sorting-factor' parameter value");
        }
        sortFactorAttr = sortFactorAttr + " " + sortOrderAttr;

        //query list
        List<Request> requestList = new ArrayList<>();
        List<Request> tempList;
        int pageCounter = page;
        try {
            do {
                logger.trace("Prepared query : userId={}, chunkSize={}, page={}, sortFactor={}",
                        user.getId(), chunkSize, page, sortFactorAttr);
                tempList = EntityManager.requestGetByClient(user.getId(), chunkSize, pageCounter, sortFactorAttr);
                logger.trace("Temp list : {}", tempList);
                if (tempList.isEmpty()) {
                    break;
                }
                if (filterFactor != null) {
                    final Request.Status finalFilterFactor = filterFactor;
                    requestList.addAll(
                            tempList.stream()
                                    .filter(n -> n.getStatus() == (finalFilterFactor))
                                    .collect(Collectors.toList()));
                }
                logger.trace("Current iteration requests count: {}", requestList.size());
                pageCounter++;
            } while (requestList.size() < chunkSize);
        } catch (DBException ex) {
            logger.error("Cannot get Request List", ex);
            session.setAttribute("error", ex.getPublicMessage());
            return PagePath.ERROR;
        }
        logger.trace("Retrieved: {}", requestList);
        //increasing page to avoid presenting user 0-page
        req.setAttribute("currentPage", ++page);
        //result list is in reverse order to queried
        req.setAttribute("requestList", requestList);
        return PagePath.CLIENT_REQUESTS;
    }

    private static boolean isSortingFactorValid(String sortFactor) {
        switch (sortFactor) {
            case "id":
            case "creation_date":
            case "status":
            case "price":
            case "completion_date":
                return true;
            default:
                return false;
        }
    }

}
