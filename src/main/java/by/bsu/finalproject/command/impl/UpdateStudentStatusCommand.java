package by.bsu.finalproject.command.impl;

import by.bsu.finalproject.command.ActionCommand;
import by.bsu.finalproject.command.MessageName;
import by.bsu.finalproject.command.ParamName;
import by.bsu.finalproject.command.PathName;
import by.bsu.finalproject.exception.CommandException;
import by.bsu.finalproject.exception.ServiceException;
import by.bsu.finalproject.manager.ConfigurationManager;
import by.bsu.finalproject.manager.MessageManager;
import by.bsu.finalproject.service.impl.StudentServiceImpl;
import by.bsu.finalproject.service.impl.PaymentServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * Update students pay status command
 * @author A. Kuzmik
 */

public class UpdateStudentStatusCommand implements ActionCommand {

    private StudentServiceImpl logic = new StudentServiceImpl();

    @Override
    public String execute(HttpServletRequest request) throws CommandException {

        String page = ConfigurationManager.getProperty(PathName.ADMIN_CHOOSE_STUDENT_STATUS_PAGE);

        int userId = Integer.parseInt(request.getParameter(ParamName.USER_ID));
        int status = Integer.parseInt(request.getParameter(ParamName.STATUS));
        String redirect = request.getParameter(ParamName.REDIRECT);

        boolean wasUpdated;

        try {
            wasUpdated = logic.updatePaymentStatus(userId, status);
            if (wasUpdated) {
                request.setAttribute(ParamName.REDIRECT, redirect);
            } else {
                PaymentServiceImpl paymentService = new PaymentServiceImpl();
                request.setAttribute(ParamName.STATUS, paymentService.selectStatuses());
                request.setAttribute(ParamName.USER_ID, userId);
                request.setAttribute(ParamName.INFO, MessageManager.getProperty(MessageName.MESSAGE_WRONG_FIELDS));
            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
