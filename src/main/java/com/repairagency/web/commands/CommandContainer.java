package com.repairagency.web.commands;

import com.repairagency.web.commands.impl.*;
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
        commandMap.put(Names.CHECK_PHONE, new CheckPhone());
        commandMap.put(Names.CHANGE_CLIENT_SETTINGS, new ChangeClientSettings());
        commandMap.put(Names.CHANGE_PASSWORD, new ChangePassword());
        commandMap.put(Names.GET_REQUEST_LIST, new GetRequestList());
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
