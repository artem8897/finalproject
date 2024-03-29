package by.bsu.finalproject.command;

import by.bsu.finalproject.command.implpage.LoginPageCommand;
import by.bsu.finalproject.manager.MessageManager;

import javax.servlet.http.HttpServletRequest;

/**
 * Action factory class for define command
 * @author A. Kuzmik
 */

public class ActionFactory {

    private ActionFactory(){

    }
    /**
     * Defines command
     * @return ActionCommand
     */

    public static ActionCommand defineCommand(HttpServletRequest request) {

        ActionCommand current ;

        String action = request.getParameter(ParamName.PARAM_NAME_COMMAND);

        if (action == null || action.isEmpty()) {
            return new LoginPageCommand();
        }

        try {
            CommandType currentEnum = CommandType.valueOf(action.toUpperCase());
            current = currentEnum.getCurrentCommand();
        } catch (IllegalArgumentException e) {
            current = new LoginPageCommand();
        }

        return current;
    }
}