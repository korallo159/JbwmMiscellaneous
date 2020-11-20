package koral.jbwmmiscellaneous;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.HashMap;
import java.util.Map;



public class NewbieProtect implements Listener, CommandExecutor {
    private Map<String, Long> protect = new HashMap<>();
    private ConfigManager newbieProtect = new ConfigManager("newbieProtect.yml");

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPlayedBefore()) {
            protect.put(p.getUniqueId().toString(), System.currentTimeMillis() / 1000 + newbieProtect.config.getLong("ProtectTime"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    newbieProtect.config.getString("NewbieProtectJoinMessage") + newbieProtect.config.getLong("ProtectTime") /60 + " minut"));
            p.sendMessage("ConfigName" + newbieProtect.configFile.getName());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        Entity damaged = e.getEntity();
        if (damaged instanceof Player && damager instanceof Player) {
            if (protect.containsKey(damaged.getUniqueId().toString()))
                if (protect.get(damaged.getUniqueId().toString()) > System.currentTimeMillis() / 1000) {
                    e.setCancelled(true);
                }
            if(protect.containsKey(damager.getUniqueId().toString()))
                if(protect.get(damager.getUniqueId().toString()) > System.currentTimeMillis() / 1000){
                    e.setCancelled(true);
                }
        }
        }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("newbie") && args[0].equals("reload")) {
            newbieProtect.saveCustomDefaultConfig();
            newbieProtect.reloadCustomConfig();
            sender.sendMessage("przeladowano config");
        }
        return false;
    }
}
