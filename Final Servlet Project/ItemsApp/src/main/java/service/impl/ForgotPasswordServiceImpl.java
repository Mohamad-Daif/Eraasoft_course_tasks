package service.impl;

import exception.UserNotFoundException;
import model.User;
import repo.UserRepo;
import repo.impl.UserRepoImpl;
import service.ForgotPasswordService;

import java.sql.SQLException;
import java.util.UUID;

public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    UserRepo userRepo = new UserRepoImpl();

    @Override
    public String updateUserPasswordWithTempPassword(String username) throws SQLException {

        User user = userRepo.getUserByUsername(username);
        String tempGeneratedPassword = generateRandomPassword();
        if (user != null) {

            userRepo.updateUserPasswordWithTempPassword(user.getId(), tempGeneratedPassword);
        } else {
            throw new UserNotFoundException();
        }
        return tempGeneratedPassword;
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString();
    }

}
