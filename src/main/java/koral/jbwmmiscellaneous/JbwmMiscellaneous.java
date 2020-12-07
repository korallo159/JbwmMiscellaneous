package koral.jbwmmiscellaneous;

import koral.jbwmmiscellaneous.database.CreateTables;
import koral.jbwmmiscellaneous.database.DatabaseConnection;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class JbwmMiscellaneous extends JavaPlugin {
    public static JbwmMiscellaneous jbwmMiscellaneous;

    @Override
    public void onEnable() {
        jbwmMiscellaneous = this;
        new ModuleManager();
        saveDefaultConfig();
        if(getConfig().getBoolean("DatabaseEnabled")) {
            DatabaseConnection.connectToDatabase();
            CreateTables.createStatsTable();
        }

        // Plugin startup logic

    }
    public static JbwmMiscellaneous getJbwmMiscellaneous() {
        return jbwmMiscellaneous;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
