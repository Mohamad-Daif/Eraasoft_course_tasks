package mapper;

import static constant.DBConstant.*;
import model.Item;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetMapper {


    public static Item mapResultSetToItem(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setId(resultSet.getInt(ID_COL));
        item.setName(resultSet.getString(NAME_COL));
        item.setPrice(resultSet.getDouble(PRICE_COL));
        item.setTotalNumber(resultSet.getInt(TOTAL_NUM_COL));
        return item;
    }

    public static User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt(ID_COL));
        user.setUsername(resultSet.getString(USER_NAME_COL));
        user.setPassword(resultSet.getString(PASSWORD_COL));
        return user;
    }
}
