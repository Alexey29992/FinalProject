package com.repairagency.web.filter.access;

import com.repairagency.bean.User;

import javax.servlet.annotation.WebFilter;

@WebFilter("/main/role-dependent/manager/*")
public class ManagerFilter extends BasicAccessFilter {

    private static final User.Role accessibleRole1 = User.Role.ADMIN;
    private static final User.Role accessibleRole2 = User.Role.MANAGER;

    @Override
    protected boolean isRoleAppropriate(User.Role role) {
        return accessibleRole1.equals(role) || accessibleRole2.equals(role);
    }

}
