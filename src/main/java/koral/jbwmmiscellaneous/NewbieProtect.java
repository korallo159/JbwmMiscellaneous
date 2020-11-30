package koral.jbwmmiscellaneous;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class NewbieProtect extends CommandManager implements Listener {
    private Map<String, Long> protect = new HashMap<>();
    private ConfigManager newbieProtect = new ConfigManager("newbieProtect.yml");

    public NewbieProtect(){
        super("newbie");
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        runActionBar(p);
        if (!p.hasPlayedBefore()) {
            protect.put(p.getUniqueId().toString(), System.currentTimeMillis() / 1000 + newbieProtect.config.getLong("ProtectTime"));
            runActionBar(p);
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
                    damager.sendMessage(ChatColor.RED + "Ten gracz posiada jeszcze ochronę początkujących!");
                    e.setCancelled(true);
                }
        }
        }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("newbie") && args.length == 0){
            sender.sendMessage(ChatColor.RED + "Wpisz jakiś argument");
            return true;
        }

        if(command.getName().equals("newbie") && args[0].equals("reload")) {
            newbieProtect.reloadCustomConfig();
            sender.sendMessage("przeladowano config");
            return true;
        }

        return false;
    }

    private void runActionBar(Player p) {
        if (protect.containsKey(p.getUniqueId().toString())) {
            BukkitTask bukkitTask = new BukkitRunnable() {
                long time = protect.get(p.getUniqueId().toString()) / 1000000000 + 29;
                @Override
                public void run() {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(ChatColor.GREEN + "Pozostały czas ochrony " + ChatColor.AQUA  + time-- / 60 + " minut"));
                    if (protect.get(p.getUniqueId().toString()) < System.currentTimeMillis() / 1000) {
                        cancel();
                        protect.remove(p.getUniqueId().toString());
                    }


                }
            }.runTaskTimerAsynchronously(JbwmMiscellaneous.getJbwmMiscellaneous(), 0, 20);
        }
        else return;
    }

}
