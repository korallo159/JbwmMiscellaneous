package koral.jbwmmiscellaneous.modules;
import koral.jbwmmiscellaneous.JbwmMiscellaneous;
import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ConfigManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        itemMeta.setDisplayName("§e§lZlotaMoneta");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§8Frakcjonawkowa Waluta");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public int getValue(Player player) {
        int toDelete = 0;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.getItemMeta().getDisplayName().equals("§e§lZlotaMoneta")) {
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
            Player player = (Player) sender;
            if (cmd.getName().equals("bet") && args.length == 2 && isInt(args[1])) {
                if (betsConfig.getConfig().contains(args[0])) {
                    int total = getValue(player);
                    if (total >= Integer.valueOf(args[1])) {
                        for (int i = 0; i < Integer.valueOf(args[1]); i++)
                            player.getInventory().removeItemAnySlot(frakcjowaluta());
                        if (betsConfig.getConfig().contains(args[0] + "." + player.getName())) {
                            int value = betsConfig.getConfig().getInt(args[0] + "." + sender.getName());
                            betsConfig.getConfig().set(args[0] + "." + player.getName(), value + Integer.valueOf(args[1]));
                            betsConfig.save();
                        } else {
                            betsConfig.config.set(args[0] + "." + sender.getName(), Integer.valueOf(args[1]));
                            betsConfig.save();
                        }
                        player.sendMessage(ChatColor.GREEN + "Obstawiłeś " + Integer.valueOf(args[1]) + " na gracza " + args[0]);
                    } else {
                        player.sendMessage(ChatColor.RED + "Nie masz przy sobie wystarczająco waluty, aby obstawić!");
                        return true;
                    }
                } else {
                    sender.sendMessage("Taka osoba nie jest dostepna do glosowania!");
                }
            }

            switch (args[0]) {
                case "dodaj":
                    if (args.length == 2) {
                        betsConfig.config.set(args[1], "");
                        betsConfig.save();
                        sender.sendMessage("Zapisano bijącego się");
                    }
                    break;
                case "usun": {
                    if (args.length == 2)
                        betsConfig.config.set(args[1], null);
                    betsConfig.save();
                    sender.sendMessage("usunięto zawodnika");
                }
                break;
                case "reload":
                    break;
                case "info":
                    wyswietlInfo(player);
                    break;
                case "winners":
                    break;
                case "zaklady":
                    //            bets.entrySet().forEach(entry -> {
                    //              String name = Bukkit.getServer().getOfflinePlayer(UUID.fromString(entry.getKey())).getName();
                    //            sender.sendMessage("§7Obstawiający: §a" + name + "§7 ilosc: §e" + entry.getValue());
                    //      });
                    break;
                case "zamknijzaklady":
                    break;

            }
        }

        return true;
    }
    ArrayList<String> osoby = new ArrayList<>();
    double kursTarget1;
    double kursTarget2;

    public void wyswietlInfo(Player player) {
        player.sendMessage("§7Dostępni zawodnicy do obstawienia: ");
        liczKurs();
        for (String keys : betsConfig.getConfig().getKeys(false)) {
            if(keys.equals(osoby.get(0)))
            player.sendMessage("§71." + keys + " §4Kurs: " + kursTarget1);
            if(keys.equals(osoby.get(1)))
                player.sendMessage("§72." + keys + " §4Kurs: " + kursTarget2);
        }
    }
    public void liczKurs() {
        double total1target = 0;
        double total2target = 0;
        for(String key : betsConfig.getConfig().getKeys(false)){
            osoby.add(key);
        }
        for(String key : betsConfig.getConfig().getConfigurationSection(osoby.get(0) + ".").getKeys(false)){
           total1target = total1target + betsConfig.getConfig().getInt(osoby.get(0) + "." + key);
        }
        for(String key : betsConfig.getConfig().getConfigurationSection(osoby.get(1) + ".").getKeys(false)){
            total2target = total2target + betsConfig.getConfig().getInt(osoby.get(1) + "." + key);
        }

        //100 do 250
        // 100/250 == 0,4
        //1,67 - 0,67 * %
        //1,67 + 1,67 *
     }
    }