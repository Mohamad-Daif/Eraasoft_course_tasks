package repo.user;

import java.sql.ResultSet;

public interface UserRepo {

    ResultSet login(String username, String password);
    void signup(String username, String password);
}
