package service.impl;

import com.google.gson.Gson;
import constant.ConstantValues;
import exception.MissingMandatoryField;
import model.User;
import repo.UserRepo;
import repo.impl.UserRepoImpl;
import service.SignupService;
import util.UserRequestValidator;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

public class SignupServiceImpl implements SignupService {

    private UserRepo userRepo = new UserRepoImpl();
    private Gson gson = new Gson();

    @Override
    public void signup(HttpServletRequest request) throws SQLException, MissingMandatoryField, IOException {

        User user = gson.fromJson(request.getReader(),User.class);

        UserRequestValidator.validateSignupRequest(user);

        userRepo.signup(
                user.getUsername(),
                user.getPassword()
        );
    }
}
