package koral.jbwmmiscellaneous;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ListenerManager implements Listener {
   private static Listener newbieListener = new NewbieProtect();
   private static Listener votingListener = new Voting();


    public static Listener getNewbieListener() {
        return newbieListener;
    }
    public void registerEvents(){
        Bukkit.getServer().getPluginManager().registerEvents(newbieListener, JbwmMiscellaneous.getJbwmMiscellaneous());
        Bukkit.getServer().getPluginManager().registerEvents(votingListener, JbwmMiscellaneous.getJbwmMiscellaneous());
    }
}


