package service.impl;

import model.User;
import repo.UserRepo;
import repo.impl.UserRepoImpl;

import java.sql.SQLException;

public class ResetPasswordService {

    private final UserRepo userRepo = new UserRepoImpl();

    public void resetUserPassword(String username, String oldPassword, String newPassword) throws SQLException {
        User user = userRepo.getUserByUsername(username);
        userRepo.updateUserPassword(user.getId(), oldPassword, newPassword);
    }

}
