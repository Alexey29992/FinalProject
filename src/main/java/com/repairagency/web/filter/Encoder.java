package com.repairagency.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class Encoder implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }

}
