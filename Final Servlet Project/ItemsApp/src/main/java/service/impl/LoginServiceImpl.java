package service.impl;

import com.google.gson.Gson;
import constant.ConstantValues;
import model.User;
import repo.UserRepo;
import repo.impl.UserRepoImpl;
import service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class LoginServiceImpl implements LoginService {

    private UserRepo userRepo = new UserRepoImpl();
    private Gson gson = new Gson();

    @Override
    public User login(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {

        User user = gson.fromJson(request.getReader(), User.class);

        user = userRepo.login(
                user.getUsername(),
                user.getPassword()
        );

        HttpSession session = request.getSession();

        session.setAttribute(ConstantValues.USER_ID_ATTR, user.getId());

        return user;
    }
}
