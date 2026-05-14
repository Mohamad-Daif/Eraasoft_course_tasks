package mapper;

import static constant.DBConstant.*;

import model.Item;
import model.ItemDetails;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetMapper {


    public static Item mapResultSetToItem(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setId(resultSet.getLong(ID_COL));
        item.setName(resultSet.getString(NAME_COL));
        item.setPrice(resultSet.getDouble(PRICE_COL));
        item.setTotalNumber(resultSet.getInt(TOTAL_NUM_COL));
        item.setDeleted(resultSet.getBoolean(IS_DELETED_COL));
        item.setUserId(resultSet.getLong(USER_ID_COL));
        return item;
    }

    public static User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        while (resultSet.next()) {
            user.setId(resultSet.getLong(ID_COL));
            user.setUsername(resultSet.getString(USER_NAME_COL));
            user.setPassword(resultSet.getString(PASSWORD_COL));
        }
        return user;
    }

    public static ItemDetails mapResultSetToItemDetails(ResultSet resultSet) throws SQLException {
        ItemDetails itemDetail = new ItemDetails();
        itemDetail.setItemId(resultSet.getLong(ITEM_ID_COL));
        itemDetail.setDescription(resultSet.getString(DESCRIPTION_COL));
        itemDetail.setIssuedAt(resultSet.getDate(ISSUED_AT_COL).toLocalDate());
        itemDetail.setExpiredAt(resultSet.getDate(EXPIRED_AT_COL).toLocalDate());
        return itemDetail;
    }
}
