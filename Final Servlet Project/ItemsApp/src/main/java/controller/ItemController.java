package controller;

import com.google.gson.Gson;
import constant.ConstantValues;
import model.Item;
import service.ItemService;
import service.impl.ItemServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/item/*")
public class ItemController extends HttpServlet {


    private ItemService itemService = new ItemServiceImpl();
    private Gson gson = new Gson();
    private String jsonContentType = "application/json";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        // /item or /item/ → show all items for logged-in user
        if (pathInfo == null || pathInfo.equals("/")) {
            handleGetAllItemsRequest(request, response);
        } else {
            handleGetItemByIdRequest(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            itemService.addItem(req);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            itemService.updateItemById(req, resp);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            itemService.removeItemById(req);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void returnItemsOfPreLoggedInUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Item> items = (List<Item>) request.getSession().getAttribute(ConstantValues.ITEMS_ATTR);

        response.setContentType(jsonContentType);

        response.getWriter().write(gson.toJson(items));
    }

    private void handleGetItemByIdRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String itemIdPathParam = request.getPathInfo().substring(1);

        if (itemIdPathParam.matches("\\d+")) {

            int itemId = Integer.parseInt(itemIdPathParam);

            if (request.getSession().getAttribute(ConstantValues.USER_ID_ATTR) != null
                    && request.getSession().getAttribute(ConstantValues.ITEMS_ATTR) != null) {

                // /item/{id} → single item (API or detail view)
                List<Item> items = (List<Item>) request.getSession().getAttribute(ConstantValues.ITEMS_ATTR);
                items
                        .stream()
                        .filter(itm -> itm.getId() == itemId)
                        .findFirst()
                        .ifPresent(foundItem -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setContentType(jsonContentType);
                            try {
                                response.getWriter().write(gson.toJson(foundItem));
                                return;
                            } catch (IOException e) {
                                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                throw new RuntimeException(e);
                            }
                        });
            } else if (request.getSession().getAttribute(ConstantValues.USER_ID_ATTR) != null) {
                try {

                    Item foundItem = itemService.getItemById(request);

                    if (foundItem != null) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setContentType(jsonContentType);
                        response.getWriter().write(gson.toJson(foundItem));
                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/text");
            response.getWriter().write("item id must be integer value");
        }
    }

    private void handleGetAllItemsRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute(ConstantValues.USER_ID_ATTR) != null
                && request.getSession().getAttribute(ConstantValues.ITEMS_ATTR) != null) {
            returnItemsOfPreLoggedInUser(request, response);

        } else if (request.getSession().getAttribute(ConstantValues.USER_ID_ATTR) != null) {
            try {
                handleGetAllItemsRequestForLoggedUserWhichItemsAreNotStoredInSession(request, response);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private void handleGetAllItemsRequestForLoggedUserWhichItemsAreNotStoredInSession(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {

        long userId = (Long) request.getSession().getAttribute(ConstantValues.USER_ID_ATTR);
        List<Item> items = itemService.getAllItem(request);

        response.setContentType(jsonContentType);
        request.getSession().setAttribute(ConstantValues.ITEMS_ATTR, items);

        response.getWriter().write(gson.toJson(items));
    }

}