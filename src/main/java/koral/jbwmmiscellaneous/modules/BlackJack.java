package koral.jbwmmiscellaneous.modules;

import koral.jbwmmiscellaneous.JbwmMiscellaneous;
import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@ModuleManager.Moduł
public class BlackJack extends CommandManager implements Listener {
    Map<String, Long> cd = new HashMap<>();

    public BlackJack() {
        super("bj");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    int karty = 52;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int x = 1; //min
        int y = 11; // max
        Player player = (Player) sender;
        Random random2 = new Random();
        for(int i = 0; i<100; i++)
        JbwmMiscellaneous.log(random2.nextInt(y - x + 1) + x);

        if (!cd.containsKey(player.getUniqueId().toString()) || cd.get(player.getUniqueId().toString()) < System.currentTimeMillis() / 1000) {
            Random random = new Random();
            int losowa = random.nextInt(y - x + 1) + x;
            if (losowa != 1) {
                sendToNearby(player, "§8§l[BLACKJACK] §f" + player.getDisplayName() + "§8 wylosował/a kartę o wartości §e" + losowa);
            } else {
                sendToNearby(player, ("§8§l[BLACKJACK] §f" + player.getDisplayName() + "§8 wylosował/a §eASA"));
            }
            cd.put(player.getUniqueId().toString(), System.currentTimeMillis() / 1000 + 2);
        } else
            player.sendMessage("§4Musisz jeszcze odczekać chwilę, aby tego użyć.");

        return true;
    }

    @EventHandler
    public void preProcess(PlayerCommandPreprocessEvent e){
        String[] array = e.getMessage().split(" ");
        if(array[0].equalsIgnoreCase("/tellraw") && e.getMessage().contains("[BLACKJACK]"))
        {
            e.setCancelled(true);
            Bukkit.broadcastMessage("§8§l[BLACKJACK] " + ChatColor.RED + e.getPlayer().getDisplayName() + " próbował oszukać w blackjack używając komendy tellraw");
        }
    }

    private void sendToNearby(Player player, String message) {
        for (Entity entity : player.getLocation().getNearbyEntities(16, 16, 16)) {
            if (entity instanceof Player) {
                Player p = (Player) entity;
                p.sendMessage(message);
            }
        }
    }

}
