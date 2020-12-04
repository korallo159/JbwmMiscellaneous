package koral.jbwmmiscellaneous.database;

import org.bukkit.entity.Player;

import java.sql.*;

import static koral.jbwmmiscellaneous.database.DatabaseConnection.hikari;

public class PlayerStatements {
    public void createPlayerQuery(Player player) {
        Connection connection = null;

        String update = "INSERT INTO Stats (UUID, NICK) VALUES (?,?) ON DUPLICATE KEY UPDATE UUID=?";


        PreparedStatement statement = null;

        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Stats WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());
            statement = connection.prepareStatement(update);
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            statement.setString(3, player.getUniqueId().toString());
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
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
