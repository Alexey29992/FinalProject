package com.repairagency.web.filter.access;

import com.repairagency.bean.User;

import javax.servlet.annotation.WebFilter;

@WebFilter("/main/role-dependent/master/*")
public class MasterFilter extends BasicAccessFilter {

    private static final User.Role accessibleRole = User.Role.MASTER;

    @Override
    protected boolean isRoleAppropriate(User.Role role) {
        return accessibleRole.equals(role);
    }

}
