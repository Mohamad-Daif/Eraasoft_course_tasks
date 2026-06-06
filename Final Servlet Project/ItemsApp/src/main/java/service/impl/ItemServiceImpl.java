package service.impl;

import com.google.gson.Gson;
import config.DBConfig;
import constant.ConstantValues;
import constant.DBConstant;
import model.Item;
import repo.ItemRepo;
import repo.impl.ItemRepoImpl;
import service.ItemService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

// GET /baseUrl/item/{id}
// POST /baseUrl/item with request body <Item>
// PUT /baseUrl/item with request body <Item>
// DELETE /baseUrl/item/{id}

public class ItemServiceImpl implements ItemService {

    private ItemRepo itemRepo = new ItemRepoImpl();
    private Gson gson = new Gson();

    @Override
    public List<Item> getAllItem(HttpServletRequest request) throws SQLException {

        Long userId = (Long) request.getSession().getAttribute(ConstantValues.USER_ID_ATTR);

        return itemRepo.getAllItems(userId);
    }

    @Override
    public Item getItemById(HttpServletRequest request) throws SQLException {

        Long userId = (Long) request.getSession().getAttribute(ConstantValues.USER_ID_ATTR);
        // fetch item id from request
        String itemIdPathParam = request.getPathInfo().substring(1);

        if (itemIdPathParam.matches("\\d+")) {

            int itemId = Integer.parseInt(itemIdPathParam);
            return itemRepo.getItemById(userId, itemId);
        }
        return null;
    }

    @Override
    public void addItem(HttpServletRequest request) throws IOException, SQLException {

        Item addedItem = gson.fromJson(request.getReader(), Item.class);

        addedItem.setUserId((Long) request.getSession().getAttribute(ConstantValues.USER_ID_ATTR));
        addedItem.setDeleted(false);

        itemRepo.addItem(addedItem);

        reloadItemsStoredInSession(request);
    }

    @Override
    public void updateItemById(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {

            Item item = gson.fromJson(request.getReader(), Item.class);

            itemRepo.updateItem(item);

            reloadItemsStoredInSession(request);

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    @Override
    public void removeItemById(HttpServletRequest request) throws SQLException {
        // fetch item id from request
        String itemIdPathParam = request.getPathInfo().substring(1);

        if (itemIdPathParam.matches("\\d+")) {

            int itemId = Integer.parseInt(itemIdPathParam);

            itemRepo.deleteItemById(itemId);

            reloadItemsStoredInSession(request);
        }
    }

    private void reloadItemsStoredInSession(HttpServletRequest request) throws SQLException {
        Long userId = (Long) request.getSession().getAttribute(ConstantValues.USER_ID_ATTR);
        request.getSession().setAttribute(ConstantValues.ITEMS_ATTR, itemRepo.getAllItems(userId));
    }
}
