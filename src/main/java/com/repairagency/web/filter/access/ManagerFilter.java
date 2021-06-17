package com.repairagency.web.filter.access;

import com.repairagency.bean.User;

import javax.servlet.annotation.WebFilter;

@WebFilter("/main/role-dependent/manager/*")
public class ManagerFilter extends BasicAccessFilter {

    private static final User.Role accessibleRole = User.Role.MANAGER;

    @Override
    protected boolean isRoleAppropriate(User.Role role) {
        return accessibleRole.equals(role);
    }

}
