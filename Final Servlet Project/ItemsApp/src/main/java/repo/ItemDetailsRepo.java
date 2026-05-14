package repo;

import model.ItemDetails;

import java.sql.SQLException;

public interface ItemDetailsRepo {

    ItemDetails getItemDetails(Long itemId) throws SQLException;

    void addItemDetail(ItemDetails itemDetail) throws SQLException;

    void updateItemDetail(ItemDetails itemDetail) throws SQLException;

    void deleteItemDetail(Long itemId) throws SQLException;
}
