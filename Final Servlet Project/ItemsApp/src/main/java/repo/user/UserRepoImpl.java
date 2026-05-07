package repo.user;

import util.QueryExecuter;

import java.sql.ResultSet;

public class UserRepoImpl implements UserRepo{

    @Override
    public ResultSet login(String username, String password) {
        String getUserQueryByUsernameAndPassword = String.format(
                "SELECT id,username,password from users where username = '%s' and password = '%s'"
                ,username
                ,password);
        return QueryExecuter.executeQuery(getUserQueryByUsernameAndPassword);
    }

    @Override
    public ResultSet signup(String username, String password) {
        String insertNewUserQuery = String.format("INSERT INTO USERS(username,password) values('%s','%s')", username, password);
        return QueryExecuter.executeQuery(insertNewUserQuery);
    }
}
