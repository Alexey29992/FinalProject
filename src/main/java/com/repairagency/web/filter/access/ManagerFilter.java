package com.repairagency.web.filter.access;

import com.repairagency.bean.User;

/**
 * Filter that performs checking access to Manager pages
 */

public class ManagerFilter extends AbstractRoleAccessFilter {

    private static final User.Role accessibleRole1 = User.Role.ADMIN;
    private static final User.Role accessibleRole2 = User.Role.MANAGER;

    @Override
    protected boolean isRoleAppropriate(User.Role role) {
        return accessibleRole1.equals(role) || accessibleRole2.equals(role);
    }

}
