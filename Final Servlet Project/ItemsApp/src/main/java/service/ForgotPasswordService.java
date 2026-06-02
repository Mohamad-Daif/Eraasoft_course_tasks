package service;

import java.sql.SQLException;

public interface ForgotPasswordService {

    String updateUserPasswordWithTempPassword(String username) throws SQLException;
}
