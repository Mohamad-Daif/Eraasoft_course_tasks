package controller;

import exception.ErrorHandler;
import exception.MissingMandatoryField;
import service.SignupService;
import service.impl.SignupServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/signup")
public class SignupController extends HttpServlet {

    private SignupService signupService = new SignupServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            signupService.signup(req);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException | MissingMandatoryField | IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(e.getMessage());
        }
    }
}
