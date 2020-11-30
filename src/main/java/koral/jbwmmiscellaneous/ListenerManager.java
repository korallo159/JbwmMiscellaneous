package koral.jbwmmiscellaneous;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ListenerManager implements Listener {
   private static Listener newbieListener = new NewbieProtect();


    public static Listener getNewbieListener() {
        return newbieListener;
    }
    public void registerEvents(){
        Bukkit.getServer().getPluginManager().registerEvents(newbieListener, JbwmMiscellaneous.getJbwmMiscellaneous());
    }
}


