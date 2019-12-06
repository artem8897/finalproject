package by.bsu.finalproject.command.implpage;

import by.bsu.finalproject.command.ActionCommand;
import by.bsu.finalproject.command.PathName;
import by.bsu.finalproject.command.ParamName;
import by.bsu.finalproject.entity.Training;
import by.bsu.finalproject.entity.User;
import by.bsu.finalproject.manager.ConfigurationManager;
import by.bsu.finalproject.service.impl.TrainingServiceImpl;
import by.bsu.finalproject.exception.CommandException;
import by.bsu.finalproject.exception.LogicException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class ButtonTrainingPageCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        Map training;
        HttpSession session = request.getSession(true);
        User user = ((User)(session.getAttribute(ParamName.USER_SESSION)));
        TrainingServiceImpl trainingService = new TrainingServiceImpl();
        int trainingId = Integer.parseInt(request.getParameter(ParamName.TRAINING));
        try{
            training = trainingService.findTrainingById(trainingId);
            if(training == null){
                page = ConfigurationManager.getProperty(PathName.PATH_PAGE_TRAINER);
                return page;
            }
            request.setAttribute(ParamName.MOV_ATTRIBUTE, ParamName.UPDATE);
            request.setAttribute(ParamName.TRAINING, training);
            request.setAttribute(ParamName.PARAM_NAME_USER_TYPE, user.getUserType().toString());
        } catch (LogicException e) {
            throw new CommandException(e);
        }
        page = ConfigurationManager.getProperty(PathName.PATH_TRAINING_PAGE);
        return page;
    }
}
