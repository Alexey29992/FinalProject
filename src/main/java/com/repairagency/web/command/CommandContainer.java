package com.repairagency.web.command;

import com.repairagency.web.command.impl.admin.CreateUser;
import com.repairagency.web.command.impl.admin.GetAdminUsers;
import com.repairagency.web.command.impl.admin.RemoveRequest;
import com.repairagency.web.command.impl.admin.RemoveUser;
import com.repairagency.web.command.impl.client.*;
import com.repairagency.web.command.impl.common.*;
import com.repairagency.web.command.impl.manager.*;
import com.repairagency.web.command.impl.master.GetRequestsMaster;
import com.repairagency.web.command.impl.master.SetStatusMaster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * CommandContainer is an utility class that holds static {@link Map} of all available
 * {@link Command}s and a method to get the desired {@link Command} by its identifier.
 * In case of unknown identifier was provided CommandContainer will return Invalid Command handler
 */
public class CommandContainer {

    private CommandContainer() {
    }

    private static final Logger logger = LogManager.getLogger();

    private static final Map<String, Command> commandMap = new HashMap<>();

    static {
        commandMap.put(CommandNames.INVALID_COMMAND, new InvalidCommand());
        commandMap.put(CommandNames.LOGIN, new Login());
        commandMap.put(CommandNames.LOGOUT, new Logout());
        commandMap.put(CommandNames.CREATE_REQUEST, new CreateRequest());
        commandMap.put(CommandNames.CHANGE_CLIENT_SETTINGS, new ChangeClientSettings());
        commandMap.put(CommandNames.CHANGE_PASSWORD, new ChangePassword());
        commandMap.put(CommandNames.GET_CLIENT_REQUESTS, new GetRequestsClient());
        commandMap.put(CommandNames.GET_CLIENT_PAYMENT_RECORDS, new GetPaymentRecords());
        commandMap.put(CommandNames.GET_MANAGER_REQUESTS, new GetRequestsManager());
        commandMap.put(CommandNames.GET_MASTER_REQUESTS, new GetRequestsMaster());
        commandMap.put(CommandNames.FEEDBACK, new Feedback());
        commandMap.put(CommandNames.MAKE_PAYMENT, new MakePayment());
        commandMap.put(CommandNames.TOP_UP_BALANCE, new TopUpBalance());
        commandMap.put(CommandNames.GET_USER, new GetUser());
        commandMap.put(CommandNames.GET_REQUEST, new GetRequest());
        commandMap.put(CommandNames.SET_MASTER, new SetMaster());
        commandMap.put(CommandNames.SET_PRICE, new SetPrice());
        commandMap.put(CommandNames.SET_STATUS_MANAGER, new SetStatusManager());
        commandMap.put(CommandNames.SET_STATUS_MASTER, new SetStatusMaster());
        commandMap.put(CommandNames.GET_ADMIN_USERS, new GetAdminUsers());
        commandMap.put(CommandNames.CREATE_USER, new CreateUser());
        commandMap.put(CommandNames.REMOVE_USER, new RemoveUser());
        commandMap.put(CommandNames.REMOVE_REQUEST, new RemoveRequest());
    }

    /**
     * Receives {@link Command} with given key.
     * @param key key of desired Command
     * @return Command object that mapped to given key
     * or 'invalid-command' Command if given key can't be resolved
     */
    public static Command getCommand(String key) {
        logger.trace("CommandContainer#getCommand queried with command : {}", key);
        if (!commandMap.containsKey(key)) {
            logger.error("Invalid command {} was queried", key);
            return commandMap.get(CommandNames.INVALID_COMMAND);
        }
        return commandMap.get(key);
    }

}
