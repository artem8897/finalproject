package by.bsu.finalproject.command.impl;

import by.bsu.finalproject.command.ActionCommand;
import by.bsu.finalproject.command.MessageName;
import by.bsu.finalproject.command.ParamName;
import by.bsu.finalproject.command.PathName;
import by.bsu.finalproject.exception.CommandException;
import by.bsu.finalproject.exception.ServiceException;
import by.bsu.finalproject.manager.ConfigurationManager;
import by.bsu.finalproject.manager.MessageManager;
import by.bsu.finalproject.service.impl.PaymentServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * Add discount command
 * @author A. Kuzmik
 */

public class CreateDiscountCommand implements ActionCommand {

    private PaymentServiceImpl logic = new PaymentServiceImpl();

    @Override
    public String execute(HttpServletRequest request) throws CommandException {

        String page = ConfigurationManager.getProperty(PathName.PATH_ADMIN_PAGE);

        String date = request.getParameter(ParamName.PARAM_NAME_DATE);
        String discount = request.getParameter(ParamName.DISCOUNT);
        String redirect = request.getParameter(ParamName.REDIRECT);

        boolean wasCreated;

        try {
            wasCreated = logic.addDiscount(date, discount);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        if (wasCreated) {
            request.setAttribute(ParamName.REDIRECT, redirect);
        } else {
            request.setAttribute(ParamName.INFO, MessageManager.getProperty(MessageName.MESSAGE_WRONG_FIELDS));
        }
        return page;
    }
}

