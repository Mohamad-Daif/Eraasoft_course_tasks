package controller;

import constant.ConstantValues;
import exception.ErrorHandler;
import exception.ItemNotFound;
import service.ItemService;
import service.impl.ItemServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/item/*")
public class ItemController extends HttpServlet {

    ItemService itemService = new ItemServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");

        // Get the path info after /item/
        String pathInfo = request.getPathInfo();

        /**
         * In case the url is null or end with /, which means that it is /item, so get all items
         * else it means that you get by id.
         */
        if (pathInfo == null || pathInfo.equals("/")) {
            try {
                itemService.getAllItem().forEach(item -> {
                    try {
                        response.getWriter().write(item.toString());
                    } catch (IOException e) {
                        ErrorHandler.forwardToErrorPage(request, response, e.getMessage(), 500);
                    }

                });
            } catch (SQLException e) {
                ErrorHandler.forwardToErrorPage(request, response, e.getMessage(), 500);
            }
            return;
        }

        // Remove the leading slash
        String idOrName = pathInfo.substring(1);

        if (idOrName.matches("\\d+")) {
            // Search by ID
            int id = Integer.parseInt(idOrName);
            try {
                response.getWriter().write(itemService.getItemById(id).toString());
            } catch (SQLException e) {
                ErrorHandler.forwardToErrorPage(request, response, e.getMessage(), 500);
            } catch (ItemNotFound e) {
                ErrorHandler.forwardToErrorPage(request, response, e.getMessage(), 404);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        itemService.addItem(req);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }
}
