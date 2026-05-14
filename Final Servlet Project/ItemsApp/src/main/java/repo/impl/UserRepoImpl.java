package repo.impl;

import config.DBConfig;
import mapper.ResultSetMapper;
import model.User;
import repo.UserRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepoImpl implements UserRepo {

    @Override
    public User login(String username, String password) throws SQLException {

        try (Connection connection = DBConfig.getConnection()) {

            String query = "SELECT * from itemschema.users where username = ? and password = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            return ResultSetMapper.mapResultSetToUser(resultSet);
        }

    }

    @Override
    public void signup(String username, String password) throws SQLException {
      try(Connection connection = DBConfig.getConnection()) {
          String query = "INSERT INTO itemschema.users(username,password) values(?,?)";

          PreparedStatement preparedStatement = connection.prepareStatement(query);
          preparedStatement.setString(1, username);
          preparedStatement.setString(2, password);
          preparedStatement.executeUpdate();
      }
    }
}
