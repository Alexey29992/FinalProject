package com.repairagency.web.command.impl;

import com.repairagency.database.wrapper.QueryData;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.impl.parser.Parsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public abstract class GetTableContent implements Command {

    private static final Logger logger = LogManager.getLogger();

    protected int page;
    protected int size;

    protected void parseStandardParams(QueryData generator, HttpServletRequest req) {
        String sortFactorAttr = req.getParameter("sort-factor");
        logger.trace("sort-factor : {}", sortFactorAttr);
        String sortOrderAttr = req.getParameter("sort-order");
        logger.trace("sort-order : {}", sortOrderAttr);
        String sizeAttr = req.getParameter("size");
        logger.trace("size : {}", sizeAttr);
        String pageAttr = req.getParameter("page");
        logger.trace("page : {}", pageAttr);

        String sortFactor = Parsers.parseSort(sortFactorAttr);
        boolean descending = Parsers.parseOrder(sortOrderAttr);
        size = Parsers.parseSize(sizeAttr);
        page = Parsers.parsePage(pageAttr);

        generator.setSortFactor(sortFactor);
        generator.setDescending(descending);
        generator.setLimitFactor(size + 1);
        generator.setOffsetFactor(page * size);
    }

}
