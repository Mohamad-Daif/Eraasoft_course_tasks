package repo.impl;

import config.DBConfig;
import mapper.ResultSetMapper;
import model.User;
import repo.UserRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static constant.DBConstant.*;

public class UserRepoImpl implements UserRepo {

    @Override
    public User login(String username, String password) throws SQLException {

        try (Connection connection = DBConfig.getConnection()) {

            String query = String.format(
                    "SELECT %s,%s,%s from %s.%s where %s = ? and %s = ?",
                    ID_COL,
                    USER_NAME_COL,
                    PASSWORD_COL,
                    ITEM_SCHEMA_NAME,
                    USERS_TABLE_NAME,
                    USER_NAME_COL,
                    PASSWORD_COL
            );

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
          String query = String.format(
                  "INSERT INTO %s.%s(%s,%s) values(?,?)",
                  ITEM_SCHEMA_NAME,
                  USERS_TABLE_NAME,
                  USER_NAME_COL,
                  PASSWORD_COL
          );

          PreparedStatement preparedStatement = connection.prepareStatement(query);
          preparedStatement.setString(1, username);
          preparedStatement.setString(2, password);
          preparedStatement.executeUpdate();
      }
    }
}
