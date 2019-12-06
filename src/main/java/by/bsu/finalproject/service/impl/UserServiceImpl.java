package by.bsu.finalproject.service.impl;

import by.bsu.finalproject.dao.DaoFactory;
import by.bsu.finalproject.dao.impl.UserDaoImpl;
import by.bsu.finalproject.entity.User;
import by.bsu.finalproject.entity.UserType;
import by.bsu.finalproject.security.Cryptographer;
import by.bsu.finalproject.service.ServiceName;
import by.bsu.finalproject.service.UserService;
import by.bsu.finalproject.exception.DaoException;
import by.bsu.finalproject.exception.LogicException;
import by.bsu.finalproject.validator.UserValidator;

import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDaoImpl userDao = DaoFactory.INSTANCE.getUserDao();

    public Map<Integer, User> findAllUserMap(int currentPage, int recordPerPage) throws LogicException {
        Map<Integer, User> userMap ;
        UserDaoImpl userDao = new UserDaoImpl();
        try {
            userMap = userDao.findAll(currentPage, recordPerPage);
        } catch (DaoException e) {
            throw new LogicException(e);
        }
        return userMap;
    }

    public User findUserInBase(String enterLogin, String enterPass) throws LogicException {

        User user;
        try {
            user = userDao.findEntityByEmail(enterLogin);
        } catch (DaoException e) {
            throw new LogicException(e);
        }
        Cryptographer cryptographer = new Cryptographer();
        String encryptedPass = cryptographer.encrypt(enterPass);
        if(user!=null && user.getPassword().equals(encryptedPass)){
            return user;
        }else{
            return null;
        }

    }
    public boolean register(String login,String pass,String confirmedPassword,String username,String sex, Map map) throws LogicException {
        try {
            boolean isMailOrUsernameInBase = userDao.isExistMailOrUsername(login,username);

            if(isMailOrUsernameInBase){
                return false;
            }

            User user = new User();
            user.setEmail(login);
            user.setPassword(pass);
            user.setUsername(username);
            user.setUserSex(sex);

            boolean isValidUserInformation = validateUser(user, map, confirmedPassword);

            if(isValidUserInformation){

                Cryptographer cryptographer = new Cryptographer();
                String encryptedPassword = cryptographer.encrypt(pass);
                user.setPassword(encryptedPassword);

                return userDao.create(user);
            }else{
                return false;
            }
            // toDO
        } catch (DaoException e) {
            throw new LogicException(e);
        }
    }
    public boolean deleteUser(int userId) throws LogicException {

        try {
            return userDao.deleteUser(userId);
        } catch (DaoException e) {
            throw new LogicException(e);
        }
    }

    public boolean
    changeUserStatus(int userId,int adminId, String status, String userType) throws LogicException {

        UserType type = UserType.valueOf(userType);

        if(status == null || type== null || userId == adminId){
            return false;
        }
        try {
            return userDao.changeUserStatus(userId, status, type);
        } catch (DaoException e) {
            throw new LogicException(e);
        }
    }
    public boolean changePassword(int userId, String password) throws LogicException {

        if(password==null){
            return false;
        }
        try {
            return userDao.updatePassword(userId ,password);
        } catch (DaoException e) {
            throw new LogicException(e);
        }
    }
    public boolean changeUsername(int userId, String username) throws LogicException {

        if(username==null){
            return false;
        }
        try {
            return userDao.updateUsername(userId, username);
        } catch (DaoException e) {
            throw new LogicException(e);
        }
    }
    private boolean validateUser(User user, Map<String, String> map, String confirmedPass){

        boolean isLoginValid = user.getUsername() == null ? false : UserValidator.INSTANCE.isUsernameValid(user.getUsername());

        if(isLoginValid){
            map.put(ServiceName.USERNAME,user.getUsername());
        }else{
            map.put(ServiceName.USERNAME,ServiceName.WRONG_FIELD);
        }

        boolean isPassValid = user.getPassword() == null ? false : UserValidator.INSTANCE.isPasswordValid(user.getPassword())&&user.getPassword().equals(confirmedPass);

        if(isPassValid){
            map.put(ServiceName.PASSWORD,user.getPassword());
            map.put(ServiceName.CONFIRMED_PASSWORD,user.getPassword());
        }

        //toDO
        boolean isMailValid = user.getEmail() == null ? false : UserValidator.INSTANCE.isEmailValid(user.getEmail());

        if(isMailValid){
            map.put(ServiceName.MAIL,user.getEmail());
        }else{
            map.put(ServiceName.MAIL,ServiceName.WRONG_FIELD);
        }

        return isLoginValid && isPassValid && isMailValid;
    }
    public Integer findNumberOfRows () throws LogicException {

        int number ;

        try {
            number = userDao.findNumberOfRows();
        } catch (DaoException e) {
            throw new LogicException(e);
        }
        return number;
    }
}
