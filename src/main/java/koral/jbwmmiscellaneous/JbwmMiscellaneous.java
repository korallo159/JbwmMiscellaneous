package koral.jbwmmiscellaneous;

import org.bukkit.plugin.java.JavaPlugin;

public final class JbwmMiscellaneous extends JavaPlugin {
    public static JbwmMiscellaneous jbwmMiscellaneous;
    private ListenerManager listenerManager;

    @Override
    public void onEnable() {
        jbwmMiscellaneous = this;
        listenerManager = new ListenerManager();
        listenerManager.registerEvents();
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
