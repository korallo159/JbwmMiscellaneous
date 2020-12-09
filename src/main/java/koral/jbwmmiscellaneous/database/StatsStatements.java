package koral.jbwmmiscellaneous.database;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static koral.jbwmmiscellaneous.database.DatabaseConnection.hikari;

public class StatsStatements {

    public void customStatisticCreate(String s) {
        Connection connection = null;
        String update = "ALTER TABLE Stats ADD " + s + " DOUBLE DEFAULT 0";
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement(update);

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
    public void statisticRemove(String s) {
        Connection connection = null;
        String update = "ALTER TABLE Stats DROP COLUMN " + s;
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement(update);
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
    public void pushCustomStats(Player player) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Stats WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet results = statement.executeQuery();
            results.next();
            ResultSetMetaData rsmd = results.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            for (int i = 3; i < columnsNumber + 1; i++) {
                if(!rsmd.getColumnName(i).contains("x")) {
                    String update = "UPDATE Stats SET " + rsmd.getColumnName(i) + "=? WHERE UUID=?";
                    statement = connection.prepareStatement(update);
                    double statystyka;
                    statystyka = player.getStatistic(Statistic.valueOf(rsmd.getColumnName(i)));
                    statement.setDouble(1, statystyka);
                    statement.setString(2, player.getUniqueId().toString());
                    statement.execute();
                }
                else{
                    String update = "UPDATE Stats SET " + rsmd.getColumnName(i) + "=? WHERE UUID=?";
                    statement = connection.prepareStatement(update);
                    double statystyka;
                    String columnName = rsmd.getColumnName(i);
                    String[] split= columnName.split("x");
                    if(split[0].equals("KILL_ENTITY") || split[0].equals("ENTITY_KILLED_BY")) {
                        statystyka = player.getStatistic(Statistic.valueOf(split[0]), EntityType.valueOf(split[1]));
                        statement.setDouble(1, statystyka);
                        statement.setString(2, player.getUniqueId().toString());
                        statement.execute();
                    }
                    if(split[0].equals("MINE_BLOCK")){
                        statystyka = player.getStatistic(Statistic.valueOf(split[0]), Material.valueOf(split[1]));
                        statement.setDouble(1, statystyka);
                        statement.setString(2, player.getUniqueId().toString());
                        statement.execute();
                    }
                    if(split[0].equals("PICKUP") || split[0].equals("DROP") || split[0].equals("CRAFT_ITEM") || split[0].equals("BREAK_ITEM") || split[0].equals("USE_ITEM")){
                        statystyka = player.getStatistic(Statistic.valueOf(split[0]), Material.valueOf(split[1]));
                        statement.setDouble(1, statystyka);
                        statement.setString(2, player.getUniqueId().toString());
                        statement.execute();
                    }
                }
            }
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


    //TODO
    public void customAdvancedStatisticCreate(String s){
        Connection connection = null;
        String update = "ALTER TABLE Stats ADD " + s + " DOUBLE DEFAULT 0";
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement(update);

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

    //TODO porownywanie przez nazwe kolumny np Statistic_KILL_ENTITY EntityType.ZOMBIE
    public void pushCustomAdvancedStats(Player player) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Stats WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet results = statement.executeQuery();
            results.next();
            ResultSetMetaData rsmd = results.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            for (int i = 3; i < columnsNumber + 1; i++) {
                String update = "UPDATE Stats SET " + rsmd.getColumnName(i) + "=? WHERE UUID=?";
                statement = connection.prepareStatement(update);
                double statystyka;
                String columnName = rsmd.getColumnName(i);
                String[] split= columnName.split("x");
                statystyka = player.getStatistic(Statistic.valueOf(split[0]), EntityType.valueOf(split[1]));
                statement.setDouble(1, statystyka);
                statement.setString(2, player.getUniqueId().toString());
                statement.execute();

            }

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


    public static List<String> getCurrentColumNames(){
        Connection connection = null;
        PreparedStatement statement = null;
        List<String> currentCollumns = new ArrayList<>();
        try{
            connection = hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Stats");
            ResultSet results = statement.executeQuery();
            results.next();
            ResultSetMetaData rsmd = results.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            for(int i = 3; i<columnsNumber + 1; i++){
                currentCollumns.add(rsmd.getColumnName(i));
            }
        } catch (Exception e) {
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
        return currentCollumns;
    }
}
