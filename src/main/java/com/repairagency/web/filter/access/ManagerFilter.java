package com.repairagency.web.filter.access;

import com.repairagency.bean.User;

import javax.servlet.annotation.WebFilter;

@WebFilter("/main/role-dependent/manager/*")
public class ManagerFilter extends AbstractAccessFilter {

    private static final User.Role accessibleRole = User.Role.MANAGER;

    @Override
    protected User.Role getRole() {
        return accessibleRole;
    }

}
