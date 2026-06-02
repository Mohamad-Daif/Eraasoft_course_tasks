package repo;

import model.User;

import java.sql.SQLException;

public interface UserRepo {

    User login(String username, String password) throws SQLException;
    void signup(String username, String password) throws SQLException;
    void updateUserPassword(Long userId,String oldPassword,String newPassword) throws SQLException;
    void updateUserPasswordWithTempPassword(Long userId,String newPassword)throws SQLException;
    User getUserByUsername(String username) throws SQLException;
}
