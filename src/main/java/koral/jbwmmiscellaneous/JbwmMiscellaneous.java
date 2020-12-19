package koral.jbwmmiscellaneous;

import com.google.common.collect.Lists;
import koral.jbwmmiscellaneous.database.CreateTables;
import koral.jbwmmiscellaneous.database.DatabaseConnection;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import koral.jbwmmiscellaneous.sockets.Client;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

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

    public static String listToString(List<?> lista, int start, String wstawka) {
        StringBuilder s = new StringBuilder(lista.size() > start ? ""+lista.get(start) : "");
        int i=0;
        for (Object obj : lista)
            if (i++ > start)
                s.append(wstawka).append(obj == null ? null : obj.toString());
        return s.toString();
    }
    public static String listToString(Object[] lista, int start) {
        return listToString(Lists.newArrayList(lista), start, " ");
    }
    public static String listToString(Object[] lista, int start, String wstawka) {
        return listToString(Lists.newArrayList(lista), start, wstawka);
    }
    public static String listToString(List<?> lista, int start) {
        return listToString(lista, start, " ");
    }

    private static final Logger logger = Logger.getLogger("Minecraft");
    private static final String logprefix = "[Jbwm] ";
    public static void log(Object... msg) {
        logger.info(logprefix + listToString(msg, 0));
    }
    public static void warn(Object... msg) {
        logger.warning(logprefix + listToString(msg, 0));
    }
    public static void error(Object...msg) {
        logger.severe(logprefix + listToString(msg, 0));
    }

}
