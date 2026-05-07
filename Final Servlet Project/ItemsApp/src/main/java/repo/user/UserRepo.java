package repo.user;

import java.sql.ResultSet;

public interface UserRepo {

    ResultSet login(String username, String password);
    ResultSet signup(String username, String password);
}
