package koral.jbwmmiscellaneous.modules;

import koral.jbwmmiscellaneous.JbwmMiscellaneous;
import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ConfigManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
@ModuleManager.Moduł
public class LocalChat extends CommandManager implements Listener {

    ConfigManager config = new ConfigManager("LocalChat.yml");

    public LocalChat() {
        super("localchat");
        JbwmMiscellaneous.addPerm("JbwmMiscellaneous.localchat.bypass");
    }
    private static boolean isEnabled = true;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length <= 1)
            return utab(args, "bypass", "toggle");
        return null;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e ){
        Player p = e.getPlayer();
        if(p.hasPermission("JbwmMiscellaneous.localchat.bypass")){
            p.setMetadata("bypass", new FixedMetadataValue(JbwmMiscellaneous.getJbwmMiscellaneous(), true));

        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length < 1) return false;
        Player player = ((Player) sender).getPlayer();
        switch (args[0]) {
            case "reload":
                config.reloadCustomConfig();
                player.sendMessage("Przeladowano localchat");
                break;
            case "bypass":
                if (!isChatBypassing(player)) {
                    player.setMetadata("bypass", new FixedMetadataValue(JbwmMiscellaneous.getJbwmMiscellaneous(), true));
                    player.sendMessage(ChatColor.GREEN +"Możesz pisać globalnie");
                } else {
                    player.removeMetadata("bypass", JbwmMiscellaneous.getJbwmMiscellaneous());
                    player.sendMessage(ChatColor.RED + "Twoje wiadomosci sa lokalne");
                }
                break;
            case "toggle":
                    if (isEnabled) {
                        isEnabled = false;
                        Bukkit.broadcastMessage(ChatColor.GREEN + "Czat jest teraz globalny");
                    }
                    else {
                        isEnabled = true;
                        Bukkit.broadcastMessage(ChatColor.RED + "Czat jest znowu lokalny");

                    }
                    break;
        }
        return true;
    }
    boolean isChatBypassing(Player p) {
        return p.hasMetadata("bypass");
    }

    @EventHandler
    public void onPlayerNearbyChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if(isEnabled) {
            if (!isChatBypassing(p)) {
                int distance = config.getConfig().getInt("messagedistance");
                e.getRecipients().removeIf(player -> player.getLocation().distance(e.getPlayer().getLocation()) > distance && !player.hasMetadata("bypass"));

            } else return;
        }
        else return;
    }
}
