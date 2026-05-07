package exception;

import constant.ConstantValues;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorHandler {

    public static void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response, String message, int code) {
        // Forward to error page with exception data
        request.setAttribute(ConstantValues.STATUS_CODE_ATTR, code);
        request.setAttribute(ConstantValues.MESSAGE_ATTR, message);

        // Forward keeps the URL, user sees clean error page
        try {
            request.getRequestDispatcher(ConstantValues.ERROR_PAGE_PATH).forward(request, response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
