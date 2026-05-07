package repo.item;

import constant.DBConstant;
import model.Item;
import util.QueryExecuter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemRepoImpl implements ItemRepo {

    @Override
    public ResultSet getAllItems() throws SQLException {
        String getAllItemsQuery = "SELECT * FROM item";
        return QueryExecuter.executeQueryWithResultSet(getAllItemsQuery);
    }

    @Override
    public ResultSet getItemById(long id) {
        String getItemByIdQuery = String.format("SELECT * FROM item WHERE id = %d", id);
        return QueryExecuter.executeQueryWithResultSet(getItemByIdQuery);
    }

    @Override
    public void addItem(Item item) {

        String addNewItemQuery = String.format("INSERT INTO item (%s,%s,%s) values(%s,%.2f,%d)",
                DBConstant.NAME_COL,
                DBConstant.PRICE_COL,
                DBConstant.TOTAL_NUM_COL,
                item.getName(),
                item.getPrice(),
                item.getTotalNumber()
        );

        QueryExecuter.executeQueryWithoutResultSet(addNewItemQuery);
    }

    @Override
    public ResultSet updateItem(Item item) {
        return null;
    }

    @Override
    public ResultSet deleteItem(String name) {
        return null;
    }

    @Override
    public ResultSet deleteItemById(long id) {
        return null;
    }

}
