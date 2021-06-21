package com.repairagency.web.command.impl.client;

import com.repairagency.bean.EntityManager;
import com.repairagency.bean.User;
import com.repairagency.bean.data.PaymentRecord;
import com.repairagency.database.DBFields;
import com.repairagency.database.QueryGetData;
import com.repairagency.exception.DBException;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import com.repairagency.web.command.Util;
import com.repairagency.web.command.impl.common.GetTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 *  Command of receiving list of payment records via Client access page
 */
public class GetPaymentRecords extends GetTable implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req) {
        logger.debug("Executing command : get-payment-records");
        String sortFactorAttr = req.getParameter("sort-factor");
        logger.trace("sort-factor : {}", sortFactorAttr);
        String sortFactor = Util.parseSortPaymentRecord(sortFactorAttr);
        HttpSession session = req.getSession();
        User client = (User) session.getAttribute("user");
        QueryGetData queryData = new QueryGetData();
        parseTableParams(queryData, req);
        queryData.setSortFactor(sortFactor);
        queryData.setFilterFactor(DBFields.PR_CLIENT_ID, String.valueOf(client.getId()));
        try {
            List<PaymentRecord> list = EntityManager.getPaymentRecordList(queryData);
            processTableParams(req, list);
            req.setAttribute("paymentRecords", list);
            return PagePath.CLIENT_PAYMENT_HISTORY;
        } catch (DBException ex) {
            logger.error("Cannot get payment records", ex);
            session.setAttribute("error", ex.getPublicMessage());
            return PagePath.ERROR;
        }
    }

}
