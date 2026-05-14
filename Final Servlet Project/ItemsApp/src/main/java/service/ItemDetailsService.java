package service;

import model.ItemDetails;

import java.sql.SQLException;

public interface ItemDetailsService {

    void addItemDetails(ItemDetails itemDetails) throws SQLException;
    void updateItemDetails(ItemDetails itemDetails) throws SQLException;
    void deleteItemDetails(Long itemId) throws SQLException;
    ItemDetails getItemDetails(Long itemId) throws SQLException;
}
