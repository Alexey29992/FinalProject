package com.repairagency.web.command;

import com.repairagency.web.command.impl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {

    private CommandContainer() {
    }

    private static final Logger logger = LogManager.getLogger();

    private static final Map<String, Command> commandMap = new HashMap<>();

    static {
        commandMap.put(Names.INVALID_COMMAND, new InvalidCommand());
        commandMap.put(Names.LOGIN, new Login());
        commandMap.put(Names.CREATE_REQUEST, new CreateRequest());
        commandMap.put(Names.CHANGE_CLIENT_SETTINGS, new ChangeClientSettings());
        commandMap.put(Names.CHANGE_PASSWORD, new ChangePassword());
        commandMap.put(Names.GET_CLIENT_REQUESTS, new GetClientRequests());
        commandMap.put(Names.GET_MANAGER_REQUESTS, new GetManagerRequests());
        commandMap.put(Names.FEEDBACK, new Feedback());
        commandMap.put(Names.MAKE_PAYMENT, new MakePayment());
        commandMap.put(Names.TOP_UP_BALANCE, new TopUpBalance());
        commandMap.put(Names.GET_USER_BY_ID, new GetUserById());
    }

    public static Command getCommand(String key) {
        logger.trace("CommandContainer#getCommand queried with command : {}", key);
        if (!commandMap.containsKey(key)) {
            logger.error("Invalid command {} was queried", key);
            return commandMap.get(Names.INVALID_COMMAND);
        }
        return commandMap.get(key);
    }

}
