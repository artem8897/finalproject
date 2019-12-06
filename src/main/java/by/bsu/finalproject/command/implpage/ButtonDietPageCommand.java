package by.bsu.finalproject.command.implpage;

import by.bsu.finalproject.command.ActionCommand;
import by.bsu.finalproject.command.PathName;
import by.bsu.finalproject.command.ParamName;
import by.bsu.finalproject.entity.Diet;
import by.bsu.finalproject.entity.User;
import by.bsu.finalproject.entity.UserType;
import by.bsu.finalproject.manager.ConfigurationManager;
import by.bsu.finalproject.manager.MessageManager;
import by.bsu.finalproject.service.impl.DietServiceImpl;
import by.bsu.finalproject.exception.CommandException;
import by.bsu.finalproject.exception.LogicException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class ButtonDietPageCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        try {
            HttpSession session = request.getSession(true);
            int userId;
            User user = ((User)(session.getAttribute(ParamName.USER_SESSION)));
            if (user.getUserType() == UserType.USER) {
                userId = user.getId();
            }else {
                userId = Integer.parseInt(request.getParameter(ParamName.USER_ID));
            }
            DietServiceImpl dietService = new DietServiceImpl();
            boolean isDietExist = dietService.isDietExist(userId);
            if(!isDietExist){
                switch (user.getUserType()){
                    case ADMIN : page = ConfigurationManager.getProperty(PathName.PATH_ADMIN_PAGE);
                    break;
                    case TRAINER : page = ConfigurationManager.getProperty(PathName.PATH_PAGE_TRAINER);
                    break;
                    case USER : page = ConfigurationManager.getProperty(PathName.PATH_USER_PAGE);
                    break;
                    default: throw new EnumConstantNotPresentException(UserType.class,"wrong role");
                }
                request.setAttribute("info", MessageManager.getProperty("message.no_diet_for_user"));

                return page;
            }
            Map diet = dietService.findUsersDiet(userId);
            request.setAttribute(ParamName.MOV_ATTRIBUTE, ParamName.UPDATE);
            request.setAttribute(ParamName.DIET, diet);
            request.setAttribute(ParamName.PARAM_NAME_USER_TYPE, user.getUserType().toString());
            request.setAttribute(ParamName.USER_ID,userId);
        } catch (LogicException e) {
            throw new CommandException(e);
        }

        page = ConfigurationManager.getProperty(PathName.PATH_DIET_PAGE);
        return page;
    }
}
