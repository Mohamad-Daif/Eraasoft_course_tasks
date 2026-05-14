package service.impl;

import com.google.gson.Gson;
import constant.ConstantValues;
import model.Item;
import repo.ItemRepo;
import repo.impl.ItemRepoImpl;
import service.ItemService;
import util.ItemRequestValidator;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ItemServiceImpl implements ItemService {

    private HttpServletRequest httpRequest;
    private Long userId;

    @Override
    public void setHttpRequest(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
        userId = (Long) httpRequest.getSession().getAttribute(ConstantValues.USER_ID_ATTR);
    }
    private ItemRepo itemRepo = new ItemRepoImpl();
    private Gson gson = new Gson();

    @Override
    public List<Item> getAllItem() throws SQLException {
        return itemRepo.getAllItems(userId);
    }

    @Override
    public Item getItemById(long itemId) throws SQLException {
        return itemRepo.getItemById(userId, itemId);
    }

    @Override
    public void addItem(HttpServletRequest request) throws IOException {

        Item addedItem = gson.fromJson(request.getReader(), Item.class);

        addedItem.setUserId((Long) httpRequest.getSession().getAttribute(ConstantValues.USER_ID_ATTR));
        addedItem.setDeleted(false);

        ItemRequestValidator.validatePostItemRequest(addedItem);

        itemRepo.addItem(addedItem);
    }

    @Override
    public void updateItemById(HttpServletRequest request) {
        ItemRequestValidator.validateUpdateItemRequest(request);

    }

    @Override
    public void removeItemById(HttpServletRequest request) {

    }
}
