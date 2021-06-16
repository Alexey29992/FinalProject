package com.repairagency.web.filter.access;

import com.repairagency.bean.User;

import javax.servlet.annotation.WebFilter;

@WebFilter("/main/role-dependent/client/*")
public class ClientFilter extends AbstractAccessFilter {

    private static final User.Role accessibleRole = User.Role.CLIENT;

    @Override
    protected User.Role getRole() {
        return accessibleRole;
    }

}
