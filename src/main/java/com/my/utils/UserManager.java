package com.my.utils;

import com.my.entities.users.User;
import com.my.entities.users.Role;
import com.my.exceptions.DBException;

import java.util.List;

public class UserManager {

    private UserManager() {
    }

    public static User getUser(int id)
            throws DBException /*id not correct*/ {
        return null;
    }

    public static User getUser(User user)
            throws DBException /*user id not correct*/ {
        return null;
    }

    public static User getUser(String login)
            throws DBException /*login not correct*/ {
        return null;
    }

    public static int addUser(User user)
            throws DBException /*user with such login already exists*/ {
        return 0;
    }

    public static boolean isLoginRegistered(String login) {
        return false;
    }

    public static List<User> getUserList(Role role)
            throws DBException {
        return null;
    }

    public static List<User> getUserList() {
        return null;
    }

    public static void updateUser(User user) {

    }

    public static void removeUser(User user) {

    }

}
