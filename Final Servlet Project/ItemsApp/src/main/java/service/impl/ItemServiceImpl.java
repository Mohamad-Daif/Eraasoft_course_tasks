package service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import constant.ConstantValues;
import exception.ExceptionModel;
import exception.ItemNotFound;
import mapper.ResultSetMapper;
import model.Item;
import repo.item.ItemRepo;
import repo.item.ItemRepoImpl;
import service.ItemService;
import util.ItemRequestValidator;

import javax.servlet.http.HttpServletRequest;

public class ItemServiceImpl implements ItemService {

    ItemRepo itemRepo = new ItemRepoImpl();
    ResultSetMapper resultSetMapper = new ResultSetMapper();

    @Override
    public List<Item> getAllItem() throws SQLException {
        ResultSet resultSet = itemRepo.getAllItems();
        List<Item> items = new ArrayList<>();
        while (resultSet.next()) {
            items.add(resultSetMapper.mapResultSetToItem(resultSet));
        }
        resultSet.close();
        return items;
    }

    @Override
    public Item getItemById(int id) throws SQLException {
        ResultSet resultSet = itemRepo.getItemById(id);
        Item item = null;
        if (!resultSet.next()) {
            throw new ItemNotFound(new ExceptionModel("There is no item with id : " + id, 404));
        } else {
            while (resultSet.next()) {
                item = resultSetMapper.mapResultSetToItem(resultSet);
            }
            resultSet.close();
            return item;
        }
    }

    @Override
    public void addItem(HttpServletRequest request) {

        ItemRequestValidator.validatePostItemRequest(request);

        Item item = new Item();
        item.setName(request.getParameter(ConstantValues.NAME_PARAM));
        item.setPrice(Double.valueOf(request.getParameter(ConstantValues.PRICE_PARAM)));
        item.setTotalNumber(Integer.valueOf(request.getParameter(ConstantValues.TOTAL_NUMBER_PARAM)));

        itemRepo.addItem(item);
    }

    @Override
    public void updateItemById(HttpServletRequest request) {
        ItemRequestValidator.validateUpdateItemRequest(request);

    }

    @Override
    public void removeItemById(HttpServletRequest request) {

    }
}
