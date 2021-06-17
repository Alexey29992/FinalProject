package com.repairagency.web.filter.access;

import com.repairagency.bean.User;

import javax.servlet.annotation.WebFilter;

@WebFilter("/main/role-dependent/admin/*")
public class AdminFilter extends BasicAccessFilter {

    private static final User.Role accessibleRole = User.Role.ADMIN;

    @Override
    protected boolean isRoleAppropriate(User.Role role) {
        return accessibleRole.equals(role);
    }

}
