package com.repairagency.web.filter.access;

import com.repairagency.bean.User;

/**
 * Filter that performs checking access to Master pages
 */

public class MasterFilter extends AbstractRoleAccessFilter {

    private static final User.Role accessibleRole = User.Role.MASTER;

    @Override
    protected boolean isRoleAppropriate(User.Role role) {
        return accessibleRole.equals(role);
    }

}
