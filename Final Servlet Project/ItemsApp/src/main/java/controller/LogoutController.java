package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Properly invalidate existing session
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Delete cookie
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        resp.addCookie(cookie);

        // Server-side redirect to login page
        resp.sendRedirect("http://localhost:8080/login.html");
    }
}