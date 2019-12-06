package by.bsu.finalproject.command.implpage;

import by.bsu.finalproject.command.ActionCommand;
import by.bsu.finalproject.command.PathName;
import by.bsu.finalproject.exception.CommandException;
import by.bsu.finalproject.manager.ConfigurationManager;
import by.bsu.finalproject.manager.MessageManager;

import javax.servlet.http.HttpServletRequest;

public class TrainerMainAccCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        request.setAttribute("info", MessageManager.getProperty("message.success_update"));

        String page = ConfigurationManager.getProperty(PathName.PATH_PAGE_TRAINER);
        return page;
    }
}
