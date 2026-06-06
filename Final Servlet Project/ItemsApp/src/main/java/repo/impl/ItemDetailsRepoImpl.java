package repo.impl;

import static constant.DBConstant.*;

import config.DBConfig;
import mapper.ResultSetMapper;
import model.ItemDetails;
import repo.ItemDetailsRepo;

import java.sql.*;

public class ItemDetailsRepoImpl implements ItemDetailsRepo {

    @Override
    public ItemDetails getItemDetails(Long itemId) throws SQLException {

        ItemDetails itemDetails = null;

        String getItemDetailsQuery = String.format(
                "SELECT %s,%s,%s,%s FROM %s.%s where %s = ?",
                ITEM_ID_COL,
                DESCRIPTION_COL,
                ISSUED_AT_COL,
                EXPIRED_AT_COL,
                ITEM_SCHEMA_NAME,
                ITEM_DETAILS_TABLE_NAME,
                ITEM_ID_COL
        );
        try (Connection connection = DBConfig.getConnection()) {

            PreparedStatement getItemDetailsStatement = connection.prepareStatement(getItemDetailsQuery);
            getItemDetailsStatement.setLong(1, itemId);
            ResultSet resultSet = getItemDetailsStatement.executeQuery();

            while (resultSet.next()) {
                itemDetails = ResultSetMapper.mapResultSetToItemDetails(resultSet);
            }
        }
        return itemDetails;
    }

    @Override
    public void addItemDetail(ItemDetails itemDetail) throws SQLException {
        String addItemDetailsQuery = String.format(
                "INSERT INTO %s(%s,%s,%s,%s) values (?,?,?,?)"
                , ITEM_DETAILS_TABLE_NAME
                , ITEM_ID_COL
                , DESCRIPTION_COL
                , ISSUED_AT_COL
                , EXPIRED_AT_COL
        );

        try (Connection connection = DBConfig.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(addItemDetailsQuery);

            preparedStatement.setLong(1, itemDetail.getItemId());
            preparedStatement.setString(2, itemDetail.getDescription());
            preparedStatement.setDate(3, Date.valueOf(itemDetail.getIssuedAt()));
            preparedStatement.setDate(4, Date.valueOf(itemDetail.getExpiredAt()));

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }

    }

    @Override
    public void updateItemDetail(ItemDetails itemDetail) throws SQLException {
        String updateItemDetailsQuery = String.format(
                "UPDATE %s set %s = ?, %s = ? , %s = ? where %s = ?"
                , ITEM_DETAILS_TABLE_NAME
                , DESCRIPTION_COL
                , ISSUED_AT_COL
                , EXPIRED_AT_COL
                , ITEM_ID_COL);

        try (Connection connection = DBConfig.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(updateItemDetailsQuery);

            preparedStatement.setString(1, itemDetail.getDescription());
            preparedStatement.setDate(2, Date.valueOf(itemDetail.getIssuedAt()));
            preparedStatement.setDate(3, Date.valueOf(itemDetail.getExpiredAt()));
            preparedStatement.setLong(4, itemDetail.getItemId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
    }

    @Override
    public void deleteItemDetail(Long itemId) throws SQLException {
        String deleteItemDetailsQuery = String.format(
                "DELETE FROM %s where %s = ?",
                ITEM_DETAILS_TABLE_NAME,
                ITEM_ID_COL
        );
        try (Connection connection = DBConfig.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteItemDetailsQuery);

            preparedStatement.setLong(1, itemId);
            preparedStatement.executeUpdate();

            preparedStatement.close();
        }
    }

}
