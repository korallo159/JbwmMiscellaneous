package koral.jbwmmiscellaneous.database;

import com.zaxxer.hikari.HikariDataSource;
import koral.jbwmmiscellaneous.JbwmMiscellaneous;

public class DatabaseConnection {

    public static HikariDataSource hikari;

    public static void connectToDatabase() {
        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", JbwmMiscellaneous.getJbwmMiscellaneous().getConfig().getString("host"));
        hikari.addDataSourceProperty("port", JbwmMiscellaneous.getJbwmMiscellaneous().getConfig().getInt("port"));
        hikari.addDataSourceProperty("databaseName", JbwmMiscellaneous.getJbwmMiscellaneous().getConfig().getString("database"));
        hikari.addDataSourceProperty("user", JbwmMiscellaneous.getJbwmMiscellaneous().getConfig().getString("username"));
        hikari.addDataSourceProperty("password", JbwmMiscellaneous.getJbwmMiscellaneous().getConfig().getString("password"));
    }
}
