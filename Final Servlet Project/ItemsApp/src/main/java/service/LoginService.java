package service;

import model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public interface LoginService {

    User login(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException;
}
