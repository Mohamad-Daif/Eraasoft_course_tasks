package repo;

import constant.ConstantValues;
import exception.ExceptionModel;
import exception.InternalServerError;
import model.Item;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemRepoImpl implements ItemRepo {

    private static DataSource dataSource = null;

    @Override
    public ResultSet getAllItems() throws SQLException {
        String getAllItemsQuery = "SELECT * FROM item";
        return queryExecuter(getAllItemsQuery);
    }

    @Override
    public ResultSet getItemById(long id) {
        String getItemByIdQuery = String.format("SELECT * FROM item WHERE id = %d", id);
        return queryExecuter(getItemByIdQuery);
    }

    @Override
    public ResultSet addItem(Item item) {

        String addNewItemQuery = String.format("INSERT INTO item (%s,%s,%s) values(%s,%.2f,%d)",
                ConstantValues.NAME_PARAM,
                ConstantValues.PRICE_PARAM,
                ConstantValues.TOTAL_NUMBER_PARAM,
                item.getName(),
                item.getPrice(),
                item.getTotalNumber()
        );

        return queryExecuter(addNewItemQuery);
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

    private static DataSource getDataSource() {
        if (dataSource == null) {
            try {
                Context ctx = new InitialContext();
                dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/PostgresDB");
                System.out.println("DataSource initialized!");
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        }
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    private ResultSet queryExecuter(String query) {

        try {
            PreparedStatement pstmt = getConnection().prepareStatement(query);

            // Step 4: Execute
            ResultSet resultSet = pstmt.executeQuery();

            return resultSet;
        } catch (SQLException e) {
            throw new InternalServerError(
                    new ExceptionModel(
                            e.getMessage(),
                            500
                    )
            );
        }
    }
}
