package repo.impl;

import config.DBConfig;
import exception.ExceptionModel;
import exception.InternalServerError;
import mapper.ResultSetMapper;
import model.Item;
import repo.ItemRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static constant.DBConstant.*;

public class ItemRepoImpl implements ItemRepo {

    @Override
    public List<Item> getAllItems(long userId) throws SQLException {

        List<Item> items = new ArrayList<>();

        try (Connection connection = DBConfig.getConnection()) {
            String getAllItemsQuery = String.format("SELECT * FROM itemschema.item where %s = ?", USER_ID_COL);

            PreparedStatement pstmt = connection.prepareStatement(getAllItemsQuery);

            pstmt.setLong(1, userId);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Item item = ResultSetMapper.mapResultSetToItem(resultSet);
                items.add(item);
            }

            return items;

        } catch (SQLException e) {
            throw new InternalServerError(
                    new ExceptionModel(
                            e.getMessage(),
                            500
                    )
            );
        }
    }

    @Override
    public Item getItemById(long userId, long id) {
        try (Connection connection = DBConfig.getConnection()) {

            String getItemByIdQuery = String.format(
                    "SELECT * FROM itemschema.item WHERE %s = ? and %s = ?",
                    USER_ID_COL,
                    ID_COL);

            PreparedStatement pstmt = connection.prepareStatement(getItemByIdQuery);

            pstmt.setLong(1, userId);
            pstmt.setLong(2, id);

            ResultSet resultSet = pstmt.executeQuery();

            Item item = null;
            while (resultSet.next()) {
                item = ResultSetMapper.mapResultSetToItem(resultSet);
            }

            return item;

        } catch (SQLException e) {
            throw new InternalServerError(
                    new ExceptionModel(
                            e.getMessage(),
                            500
                    )
            );
        }
    }

    @Override
    public void addItem(Item item) {

        try(Connection connection = DBConfig.getConnection()){
            String addNewItemQuery = String.format(
                    "INSERT INTO item (%s,%s,%s,%s,%s) values(?,?,?,?,?)"
                    , NAME_COL
                    , PRICE_COL,
                    TOTAL_NUM_COL,
                    IS_DELETED_COL,
                    USER_ID_COL);

            PreparedStatement pstmt = connection.prepareStatement(addNewItemQuery);
            pstmt.setString(1, item.getName());
            pstmt.setDouble(2, item.getPrice());
            pstmt.setInt(3,item.getTotalNumber());
            pstmt.setBoolean(4, item.getDeleted());
            pstmt.setLong(5, item.getUserId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new InternalServerError(
                    new ExceptionModel(
                            e.getMessage(),
                            500
                    )
            );
        }
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
