package util;

import constant.ConstantValues;
import exception.MissingMandatoryField;
import model.User;

import javax.servlet.http.HttpServletRequest;

public class UserRequestValidator {

    public static void validateSignupRequest(User user)throws MissingMandatoryField{
        if (
                user.getUsername() == null ||
                        user.getPassword() == null) {
            throw new MissingMandatoryField();
        }
    }

    public static void validateLoginRequest(HttpServletRequest request){
        if (
                request.getParameter(ConstantValues.USERNAME) == null ||
                        request.getParameter(ConstantValues.PASSWORD) == null) {
            throw new MissingMandatoryField();
        }
    }
}
