package controller;

import constant.ConstantValues;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute(ConstantValues.USER_ID_ATTR,null);
        req.getSession().setAttribute(ConstantValues.ITEMS_ATTR,null);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
