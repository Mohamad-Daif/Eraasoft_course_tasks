package repo;

import model.User;

import java.sql.SQLException;

public interface UserRepo {

    User login(String username, String password) throws SQLException;
    void signup(String username, String password) throws SQLException;
}
