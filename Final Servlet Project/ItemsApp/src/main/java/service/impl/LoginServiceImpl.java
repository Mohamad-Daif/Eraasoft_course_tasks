package service.impl;

import constant.ConstantValues;
import mapper.ResultSetMapper;
import model.User;
import repo.user.UserRepo;
import repo.user.UserRepoImpl;
import service.LoginService;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginServiceImpl implements LoginService {

    private UserRepo userRepo = new UserRepoImpl();

    @Override
    public User login(HttpServletRequest request) throws SQLException {
       ResultSet resultSet =  userRepo.login(
                request.getParameter(ConstantValues.USERNAME),
                request.getParameter(ConstantValues.PASSWORD)
        );

       return ResultSetMapper.mapResultSetToUser(resultSet);
    }
}
