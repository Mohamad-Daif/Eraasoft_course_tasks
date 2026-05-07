package controller;

import exception.ErrorHandler;
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
            System.out.println("User registered successfully");
        } catch (SQLException e) {
            ErrorHandler.forwardToErrorPage(req, resp, e.getMessage(), 404);
        }
    }
}
