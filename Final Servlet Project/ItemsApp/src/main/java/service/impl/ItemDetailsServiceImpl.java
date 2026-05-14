package service.impl;

import model.ItemDetails;
import repo.ItemDetailsRepo;
import repo.impl.ItemDetailsRepoImpl;
import service.ItemDetailsService;

import java.sql.SQLException;

public class ItemDetailsServiceImpl implements ItemDetailsService {

    private ItemDetailsRepo itemDetailsRepo = new ItemDetailsRepoImpl();

    @Override
    public void addItemDetails(ItemDetails itemDetails) throws SQLException {
        itemDetailsRepo.addItemDetail(itemDetails);
    }

    @Override
    public void updateItemDetails(ItemDetails itemDetails) throws SQLException {
        itemDetailsRepo.updateItemDetail(itemDetails);
    }

    @Override
    public void deleteItemDetails(Long itemId) throws SQLException {
        itemDetailsRepo.deleteItemDetail(itemId);
    }

    @Override
    public ItemDetails getItemDetails(Long itemId) throws SQLException {
        return itemDetailsRepo.getItemDetails(itemId);
    }
}
