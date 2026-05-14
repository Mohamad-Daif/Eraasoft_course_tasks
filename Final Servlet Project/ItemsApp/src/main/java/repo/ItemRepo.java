package repo;

import model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ItemRepo {

    List<Item> getAllItems(long userId) throws SQLException;
    Item getItemById(long userId,long itemId);

    void addItem(Item item);

    ResultSet updateItem(Item item);

    ResultSet deleteItem(String name);
    ResultSet deleteItemById(long id);

}
