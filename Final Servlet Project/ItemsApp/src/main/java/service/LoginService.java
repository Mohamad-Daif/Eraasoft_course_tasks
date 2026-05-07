package service;

import model.User;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public interface LoginService {

    User login(HttpServletRequest request) throws SQLException;
}
