package com.repairagency.web.filter.access;

import com.repairagency.bean.User;

import javax.servlet.annotation.WebFilter;

@WebFilter("/main/role-dependent/admin/*")
public class AdminFilter extends AbstractAccessFilter {

    private static final User.Role accessibleRole = User.Role.ADMIN;

    @Override
    protected User.Role getRole() {
        return accessibleRole;
    }

}
