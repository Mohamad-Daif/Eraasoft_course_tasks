package mapper;

import model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetMapper {

    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String PRICE_COL = "price";
    private static final String TOTAL_NUM_COL = "totalNumber";

    public Item mapResultSetToItem(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setId(resultSet.getInt(ID_COL));
        item.setName(resultSet.getString(NAME_COL));
        item.setPrice(resultSet.getDouble(PRICE_COL));
        item.setTotalNumber(resultSet.getInt(TOTAL_NUM_COL));
        return item;
    }
}
