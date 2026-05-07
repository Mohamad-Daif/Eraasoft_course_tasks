package util;

import constant.ConstantValues;
import exception.ExceptionModel;
import exception.MissingMandatoryField;

import javax.servlet.http.HttpServletRequest;

public class UserRequestValidator {

    public static void validateSignupRequest(HttpServletRequest request)throws MissingMandatoryField{
        if (
                request.getParameter(ConstantValues.USERNAME) == null ||
                        request.getParameter(ConstantValues.PASSWORD) == null) {
            throw new MissingMandatoryField(new ExceptionModel(
                    "Request body is Missing mandatory field",
                    404
            ));
        }
    }

    public static void validateLoginRequest(HttpServletRequest request){
        if (
                request.getParameter(ConstantValues.USERNAME) == null ||
                        request.getParameter(ConstantValues.PASSWORD) == null) {
            throw new MissingMandatoryField(new ExceptionModel(
                    "Request body is Missing mandatory field",
                    404
            ));
        }
    }
}
