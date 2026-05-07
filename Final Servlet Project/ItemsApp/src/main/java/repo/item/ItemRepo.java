package repo.item;

import model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ItemRepo {

    ResultSet getAllItems() throws SQLException;
    ResultSet getItemById(long id);

    ResultSet addItem(Item item);

    ResultSet updateItem(Item item);

    ResultSet deleteItem(String name);
    ResultSet deleteItemById(long id);

}
