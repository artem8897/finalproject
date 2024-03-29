package by.bsu.finalproject.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator student information
 * @author A. Kzumik
 */

public enum StudentInformationValidator {

    INSTANCE;

    private static final String REGULAR_NAME = "(([А-Яа-я]{2,14})|([A-Za-z]{2,20}))";
    private static final String REGULAR_SEX = "[A-Za-z]{4,10}";
    private static final String REGULAR_NUMBER = "\\d{1,9}";

    /**
     * Validate name
     * @param name
     * @return true if the string name is valid
     */

    public boolean isValidName(String name){
        Pattern pat = Pattern.compile(REGULAR_NAME);
        Matcher matcher = pat.matcher(name);
        return matcher.matches();

    }

    /**
     * Validate students height
     * @param heightString
     * @return true if the height is valid
     */

    public boolean isValidHeight(String heightString) {

        Pattern pat = Pattern.compile(REGULAR_NUMBER);
        Matcher matcher = pat.matcher(heightString);
        if(matcher.matches()){
            int height = Integer.parseInt(heightString);
            return height > 110 && height < 250;
        }else{
            return false;
        }
    }

    /**
     * Validate students weight
     * @param weightString
     * @return true if the weight is valid
     */

    public boolean isValidWeight(String weightString){

        Pattern pat = Pattern.compile(REGULAR_NUMBER);
        Matcher matcher = pat.matcher(weightString);
        if(matcher.matches()){
            int weight = Integer.parseInt(weightString);
            return weight > 40 && weight < 160;
        }else{
            return false;
        }
    }

    /**
     * Validate students sex
     * @param sex
     * @return true if the sex is valid
     */

    public boolean isValidSex(String sex){
        Pattern pat = Pattern.compile(REGULAR_SEX);
        Matcher matcher = pat.matcher(sex);
        return matcher.matches();
    }
}
