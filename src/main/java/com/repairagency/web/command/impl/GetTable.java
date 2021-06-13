package com.repairagency.web.command.impl;

import com.repairagency.database.QueryGetData;
import com.repairagency.web.command.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class GetTable {

    private static final Logger logger = LogManager.getLogger();

    protected int page;
    protected int size;

    public void processTableParams(HttpServletRequest req, List<?> data) {
        boolean hasNextPage = false;
        boolean hasPrevPage = false;
        if (data.size() == size + 1) {
            data.remove(size - 1);
            hasNextPage = true;
        }
        if (page > 0) {
            hasPrevPage = true;
        }
        req.setAttribute("page", page);
        req.setAttribute("hasNextPage", hasNextPage);
        req.setAttribute("hasPrevPage", hasPrevPage);
    }

    protected void parseTableParams(QueryGetData queryData, HttpServletRequest req) {
        String sortFactorAttr = req.getParameter("sort-factor");
        logger.trace("sort-factor : {}", sortFactorAttr);
        String sortOrderAttr = req.getParameter("sort-order");
        logger.trace("sort-order : {}", sortOrderAttr);
        String sizeAttr = req.getParameter("size");
        logger.trace("size : {}", sizeAttr);
        String pageAttr = req.getParameter("page");
        logger.trace("page : {}", pageAttr);

        String sortFactor = Util.parseSort(sortFactorAttr);
        String sortOrder = Util.parseOrder(sortOrderAttr);
        size = Util.parseSize(sizeAttr);
        page = Util.parsePage(pageAttr);

        queryData.setSortFactor(sortFactor);
        queryData.setSortOrder(sortOrder);
        queryData.setLimitFactor(size + 1);
        queryData.setOffsetFactor(page * size);
    }

}
