package by.bsu.finalproject.command.impl;
import by.bsu.finalproject.command.ActionCommand;
import by.bsu.finalproject.command.MessageName;
import by.bsu.finalproject.command.PathName;
import by.bsu.finalproject.command.ParamName;
import by.bsu.finalproject.manager.MessageManager;
import by.bsu.finalproject.service.impl.StudentServiceImpl;
import by.bsu.finalproject.manager.ConfigurationManager;
import by.bsu.finalproject.service.impl.TrainerServiceImpl;
import by.bsu.finalproject.service.impl.UserServiceImpl;
import by.bsu.finalproject.entity.UserType;
import by.bsu.finalproject.entity.User;
import by.bsu.finalproject.exception.CommandException;
import by.bsu.finalproject.exception.ServiceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Log in application command
 * @author A. Kuzmik
 */

public class LoginCommand implements ActionCommand {

    private StudentServiceImpl informationService = new StudentServiceImpl();
    private TrainerServiceImpl trainerService = new TrainerServiceImpl();
    private UserServiceImpl userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest request) throws CommandException {

        String page = ConfigurationManager.getProperty(PathName.PATH_LOGIN_PAGE);
        String email = request.getParameter(ParamName.PARAM_NAME_EMAIL);
        String pass = request.getParameter(ParamName.PARAM_NAME_PASSWORD);

        try {
           User user = userService.findUserInBase(email, pass);

            if (user != null) {

                HttpSession session = request.getSession(true);
                session.setAttribute(ParamName.USER_ATTRIBUTE, user);
                session.setAttribute(ParamName.PARAM_NAME_USER_TYPE, user.getUserType().toString());

                UserType userType = user.getUserType();

                switch (userType) {
                    case USER:
                        if (informationService.isExist(user.getId())) {
                            page = ConfigurationManager.getProperty(PathName.PATH_USER_PAGE);
                        } else {
                            request.setAttribute(ParamName.MOV_ATTRIBUTE, ParamName.ADD);
                            session.setAttribute(ParamName.PARAM_NAME_USER_TYPE, null);
                            page = ConfigurationManager.getProperty(PathName.PATH_PAGE_INFORMATION);
                        }
                        break;
                    case ADMIN:
                        page = ConfigurationManager.getProperty(PathName.PATH_ADMIN_PAGE);
                        break;
                    case TRAINER:
                        if (trainerService.isExist(user.getId())) {
                            page = ConfigurationManager.getProperty(PathName.PATH_PAGE_TRAINER);
                        } else {
                            request.setAttribute(ParamName.MOV_ATTRIBUTE, ParamName.ADD);
                            session.setAttribute(ParamName.PARAM_NAME_USER_TYPE, null);
                            page = ConfigurationManager.getProperty(PathName.PATH_PAGE_TRAINER_INFORMATION);
                        }
                        break;
                    default:
                        page = ConfigurationManager.getProperty(PathName.PATH_PAGE_ERROR);
                }
            } else {
                request.setAttribute(ParamName.WRONG_MAIL_OR_PASSWORD,
                        MessageManager.getProperty(MessageName.MESSAGE_LOGIN_ERROR));
            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }


}