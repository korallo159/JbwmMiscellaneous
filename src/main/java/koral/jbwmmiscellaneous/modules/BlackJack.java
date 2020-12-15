package koral.jbwmmiscellaneous.modules;

import koral.jbwmmiscellaneous.JbwmMiscellaneous;
import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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

    int distance = 20;

    private void sendToNearby(Player player, String message) {
        for (Entity entity : player.getLocation().getNearbyEntities(16, 16, 16)) {
            if (entity instanceof Player) {
                Player p = (Player) entity;
                p.sendMessage(message);
            }
        }
    }

}
