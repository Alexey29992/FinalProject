package com.repairagency.web.filter;

import com.repairagency.entity.User;
import com.repairagency.entity.user.Client;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@WebFilter("/*")
public class BalanceUpdater implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Map<Integer, Integer> updates = (Map<Integer, Integer>) httpRequest.getServletContext()
                .getAttribute("user-balance-updates");
        User user = (User) httpRequest.getSession().getAttribute("user");
        if (!(user instanceof Client)) {
            chain.doFilter(request, response);
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
        chain.doFilter(request, response);
    }

}
