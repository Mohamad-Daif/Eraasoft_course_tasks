package util;

import config.DBConfig;
import exception.ExceptionModel;
import exception.InternalServerError;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryExecuter {

    public static ResultSet executeQuery(String query) {

        try {
            PreparedStatement pstmt = DBConfig.getConnection().prepareStatement(query);

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
