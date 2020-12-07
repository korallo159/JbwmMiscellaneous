package koral.jbwmmiscellaneous.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static koral.jbwmmiscellaneous.database.DatabaseConnection.hikari;

public class CreateTables {
    public static void createStatsTable() {
        Connection connection = null;
        String create = "CREATE TABLE IF NOT EXISTS Stats(UUID varchar(36), NICK VARCHAR(16), PRIMARY KEY (UUID))";
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement(create);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null)
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }
}
