package koral.jbwmmiscellaneous.modules;
import koral.jbwmmiscellaneous.JbwmMiscellaneous;
import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ConfigManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@ModuleManager.Moduł
public class Bets extends CommandManager implements Listener {
    ConfigManager betsConfig = new ConfigManager("bets.yml");
    HashMap<String, Integer> bets = new HashMap<>();
    public Bets() {
        super("bet");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    public ItemStack frakcjowaluta() {
        ItemStack itemStack = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§eZlotaMoneta");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§8Frakcjonawkowa Waluta");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public int getValue(Player player) {
        int toDelete = 0;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.getItemMeta().getDisplayName().equals("§eZlotaMoneta")) {
                toDelete = itemStack.getAmount() + toDelete;
            }
        }

        return toDelete;

    }

    public boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (cmd.getName().equals("bet") && args.length == 1 && isInt(args[0])) {
                Player player = (Player) sender;
                int total = getValue(player);
                if (total >= Integer.valueOf(args[0])) {
                    for (int i = 0; i < Integer.valueOf(args[0]); i++)
                        player.getInventory().removeItemAnySlot(frakcjowaluta());
                    player.sendMessage(ChatColor.GREEN + "Obstawiłeś " + Integer.valueOf(args[0]) + " na gracza ");
                    int previous;
                    if (bets.containsKey(player.getUniqueId().toString())) {
                        previous = bets.get(player.getUniqueId().toString());
                        bets.put(player.getUniqueId().toString(), previous + Integer.valueOf(args[0]));

                    } else
                        bets.put(player.getUniqueId().toString(), Integer.valueOf(args[0]));
                } else {
                    player.sendMessage(ChatColor.RED + "Nie masz przy sobie wystarczająco waluty, aby obstawić!");
                    return true;
                }
            }



                switch (args[0]) {
                    case "fighter":
                        if(args.length == 2){
                            betsConfig.config.set("Fighter", "test");
                            betsConfig.save();
                            sender.sendMessage("Zapisano fightera");
                        }
                        break;
                    case "reload":
                        break;
                    case "info":
                        break;
                    case "winners":
                        break;
                    case "zaklady":
                        bets.entrySet().forEach(entry -> {
                            String name = Bukkit.getServer().getOfflinePlayer(UUID.fromString(entry.getKey())).getName();
                            sender.sendMessage("§7Obstawiający: §a" + name + "§7 ilosc: §e" + entry.getValue());
                        });
                }
            }

        return true;
    }
}