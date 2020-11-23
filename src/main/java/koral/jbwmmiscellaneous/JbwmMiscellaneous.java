package koral.jbwmmiscellaneous;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class JbwmMiscellaneous extends JavaPlugin {
    public static JbwmMiscellaneous jbwmMiscellaneous;
    public Listener listener;


    @Override
    public void onEnable() {
        jbwmMiscellaneous = this;
        listener = new NewbieProtect();
        getServer().getPluginManager().registerEvents(listener, this);
        getCommand("newbie").setExecutor(new NewbieProtect());
        getCommand("UnregisterEvent").setExecutor(new NewbieProtect());
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
