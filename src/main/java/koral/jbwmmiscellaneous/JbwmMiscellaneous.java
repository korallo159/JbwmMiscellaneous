package koral.jbwmmiscellaneous;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class JbwmMiscellaneous extends JavaPlugin {
    public static JbwmMiscellaneous jbwmMiscellaneous;


    @Override
    public void onEnable() {
        jbwmMiscellaneous = this;
        getServer().getPluginManager().registerEvents(new NewbieProtect(), this);
        getCommand("newbie").setExecutor(new NewbieProtect());
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
