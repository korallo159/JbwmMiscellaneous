package koral.jbwmmiscellaneous.modules;

import koral.jbwmmiscellaneous.JbwmMiscellaneous;
import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ModuleManager.Moduł
public class DynmapPrinting extends CommandManager implements Listener {
   private Map<String, Boolean> rysuj = new HashMap<>();
    public DynmapPrinting() {
        super("rysuj");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
                if (rysuj.containsKey(((Player) sender).getUniqueId().toString())) {
                    if(rysuj.get(((Player) sender).getUniqueId().toString()) == false) {
                        rysuj.put(((Player) sender).getUniqueId().toString(), true);
                        sender.sendMessage(ChatColor.GREEN + "włączyłeś malowanie na dynmapie");
                    }
                    else{
                        rysuj.remove(((Player) sender).getUniqueId().toString());
                        sender.sendMessage(ChatColor.RED + "wyłączyłeś malowanie na dynmapie");
                    }
                }
                else{
                    rysuj.put(((Player) sender).getUniqueId().toString(), true);
                    sender.sendMessage(ChatColor.GREEN + "włączyłeś malowanie na mapie");
                }

        }
        return true;
    }
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e){
        //przelatywanie od boku x do z
        if(e.getFrom().getBlockX() != e.getTo().getBlockX()
            //    || e.getFrom().getBlockY() != e.getTo().getBlockY()
                || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            if (rysuj.containsKey(e.getPlayer().getUniqueId().toString())) {
                if (rysuj.get(e.getPlayer().getUniqueId().toString()) == true)
                    e.getPlayer().performCommand("dmarker addcorner");
            }
        }
    }
}
