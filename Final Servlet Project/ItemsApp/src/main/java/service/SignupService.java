package service;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public interface SignupService {

    void signup(HttpServletRequest request) throws SQLException;

}
