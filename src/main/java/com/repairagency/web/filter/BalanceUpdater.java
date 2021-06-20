package com.repairagency.web.filter;

import com.repairagency.bean.User;
import com.repairagency.bean.user.Client;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Filter that updates {@link User} objects in sessions if User balance was replenished by Manager
 */

public class BalanceUpdater implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        Map<Integer, Integer> updates = (Map<Integer, Integer>) httpRequest.getServletContext()
                .getAttribute("user-balance-updates");
        User user = (User) httpRequest.getSession().getAttribute("user");
        if (!(user instanceof Client)) {
            chain.doFilter(req, resp);
            return;
        }
        Client client = (Client) user;
        int clientId = client.getId();
        Set<Map.Entry<Integer, Integer>> updatesSet = updates.entrySet();
        for (Map.Entry<Integer, Integer> update : updatesSet) {
            if (clientId == update.getKey()) {
                client.setBalance(update.getValue());
                updates.remove(update.getKey());
                break;
            }
        }
        chain.doFilter(req, resp);
    }

}
