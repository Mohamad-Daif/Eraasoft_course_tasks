package repo.impl;

import config.DBConfig;
import constant.ConstantValues;
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
        try (Connection connection = DBConfig.getConnection()) {
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

    @Override
    public void updateUserPassword(Long userId, String oldPassword, String newPassword) throws SQLException {

        User currentUser = getUserById(userId);

        if (currentUser.getPassword().equals(oldPassword)) {
            updateUserPassword(userId, newPassword);
        }
    }

    @Override
    public void updateUserPasswordWithTempPassword(Long userId, String newPassword) throws SQLException {
        updateUserPassword(userId, newPassword);
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
        try (Connection connection = DBConfig.getConnection()) {

            String query = String.format(
                    "SELECT %s,%s,%s from %s.%s where %s = ?",
                    ID_COL,
                    USER_NAME_COL,
                    PASSWORD_COL,
                    ITEM_SCHEMA_NAME,
                    USERS_TABLE_NAME,
                    USER_NAME_COL
            );

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            return ResultSetMapper.mapResultSetToUser(resultSet);
        }
    }

    private User getUserById(Long userId) throws SQLException {
        try (Connection connection = DBConfig.getConnection()) {

            String query = String.format(
                    "SELECT %s,%s,%s from %s.%s where %s = ?",
                    ID_COL,
                    USER_NAME_COL,
                    PASSWORD_COL,
                    ITEM_SCHEMA_NAME,
                    USERS_TABLE_NAME,
                    ID_COL
            );

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            return ResultSetMapper.mapResultSetToUser(resultSet);
        }
    }

    private void updateUserPassword(Long userId, String newPassword) throws SQLException {
        try (Connection connection = DBConfig.getConnection()) {
            String query = String.format(
                    "UPDATE %s.%s SET %s = ? where %s = ?",
                    ITEM_SCHEMA_NAME,
                    USERS_TABLE_NAME,
                    PASSWORD_COL,
                    ID_COL
            );
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, newPassword);
            preparedStatement.setLong(2, userId);

            preparedStatement.executeUpdate();
        }
    }
}
