package com.repairagency.web.filter.access;

import com.repairagency.bean.User;

public class ClientFilter extends AbstractRoleAccessFilter {

    private static final User.Role accessibleRole = User.Role.CLIENT;

    @Override
    protected boolean isRoleAppropriate(User.Role role) {
        return accessibleRole.equals(role);
    }

}
