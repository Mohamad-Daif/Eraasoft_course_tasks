package service.impl;

import constant.ConstantValues;
import exception.MissingMandatoryField;
import repo.user.UserRepo;
import repo.user.UserRepoImpl;
import service.SignupService;
import util.UserRequestValidator;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public class SignupServiceImpl implements SignupService {

    UserRepo userRepo = new UserRepoImpl();

    @Override
    public void signup(HttpServletRequest request) throws SQLException, MissingMandatoryField {

        UserRequestValidator.validateSignupRequest(request);

        userRepo.signup(
                request.getParameter(ConstantValues.USERNAME),
                request.getParameter(ConstantValues.PASSWORD)
        );
    }
}
