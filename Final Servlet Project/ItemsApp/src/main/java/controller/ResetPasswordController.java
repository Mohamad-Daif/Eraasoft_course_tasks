package controller;

import com.google.gson.Gson;
import model.dto.ResetPasswordDto;
import service.impl.ResetPasswordService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/resetpassword")
public class ResetPasswordController extends HttpServlet {

    private final Gson gson = new Gson();
    private final ResetPasswordService resetPasswordService = new ResetPasswordService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResetPasswordDto resetPasswordDto = gson.fromJson(req.getReader(), ResetPasswordDto.class);
        if (resetPasswordDto.currentPassword() == null || resetPasswordDto.username() == null || resetPasswordDto.newPassword() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing mandatory field within Northbound request");
        } else {
            try {
                resetPasswordService.resetUserPassword(resetPasswordDto.username(), resetPasswordDto.currentPassword(), resetPasswordDto.newPassword());
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(e.getMessage());
            }
        }
    }
}
