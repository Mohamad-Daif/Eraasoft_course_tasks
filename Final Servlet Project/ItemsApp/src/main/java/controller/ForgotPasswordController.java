package controller;

import com.google.gson.Gson;
import constant.ConstantValues;
import exception.UserNotFoundException;
import model.dto.ForgetUserDto;
import service.ForgotPasswordService;
import service.impl.ForgotPasswordServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/forgotpassword")
public class ForgotPasswordController extends HttpServlet {

    private final ForgotPasswordService forgotPasswordService = new ForgotPasswordServiceImpl();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ForgetUserDto forgetUserDto = gson.fromJson(req.getReader(), ForgetUserDto.class);

        if (forgetUserDto.username() != null) {

            try {
                String tempGeneratedPassword = forgotPasswordService.updateUserPasswordWithTempPassword(
                        forgetUserDto.username()
                );

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(tempGeneratedPassword);
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (UserNotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
