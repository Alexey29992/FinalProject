package com.repairagency.web.filter.access;

import com.repairagency.bean.User;

/**
 * Filter that performs checking access to Admin pages
 */
public class AdminFilter extends AbstractRoleAccessFilter {

    private static final User.Role accessibleRole = User.Role.ADMIN;

    @Override
    protected boolean isRoleAppropriate(User.Role role) {
        return accessibleRole.equals(role);
    }

}
