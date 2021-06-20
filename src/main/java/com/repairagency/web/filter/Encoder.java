package com.repairagency.web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filter that sets the HTTP Request encoding to UTF-8.
 * Should be processed before first request parameter was queried.
 */

public class Encoder implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }

}
