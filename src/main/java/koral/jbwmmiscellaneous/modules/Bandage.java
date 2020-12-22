package koral.jbwmmiscellaneous.modules;

import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ConfigManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import koral.jbwmmiscellaneous.util.Cooldown;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
@ModuleManager.Moduł
public class Bandage extends CommandManager implements Listener {
    HashMap<String, Long> cooldown = new HashMap<>();
    public Bandage() {
        super("dajbandaz");
        ustawKomende("dajadrenaline", "daje do eq adrenaline", Collections.emptyList());
    }
    ConfigManager bandageConfig = new ConfigManager("Bandage.yml");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if(cmd.getName().equalsIgnoreCase("dajbandaz")) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    player.getInventory().addItem(getBandage(1));
                    return true;
                }
                else if (args.length > 0) {
                    if (StringUtils.isNumeric(args[0])) {
                        player.getInventory().addItem(getBandage(Integer.valueOf(args[0])));
                        return true;
                    }
                    else {
                        final Player target = Bukkit.getServer().getPlayer(args[0]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.RED + "Taki gracz nie jest online!");
                            return true;
                        }
                        target.getInventory().addItem(getBandage(1));
                        target.sendMessage(ChatColor.GRAY + "Otrzymałeś Bandaz");
                        return true;
                    }
                }
            }
            if(cmd.getName().equalsIgnoreCase("dajadrenaline")){
                Player player = (Player) sender;
                player.getInventory().addItem(getAdrenaline(1));
                return true;
            }
            if(cmd.getName().equalsIgnoreCase("bandagereload")){
                bandageConfig.reloadCustomConfig();
                sender.sendMessage(ChatColor.GREEN + "Config został przeładowany.");
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender) {
            if (cmd.getName().equalsIgnoreCase("dajbandaz") && args.length > 0) {
                final Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Taki gracz nie jest online!");
                    return true;
                } else if(StringUtils.isNumeric(args[1]))
                    target.getInventory().addItem(getBandage(Integer.valueOf(args[1])));
                target.sendMessage(ChatColor.GRAY + "Otrzymałeś Bandaz");
                return true;
            } else sender.sendMessage("komenda niedostepna dla sendera");
        }

        return true;
    }
    public void wyleczmax(Player player)
    {
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
    }

    public ItemStack getBandage(int amount) {
        ItemStack bandage = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = bandage.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + bandageConfig.getConfig().getString("bandagename"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(bandageConfig.getConfig().getString("bandagelore"));
        itemMeta.setLore(lore);
        itemMeta.setCustomModelData(1);
        bandage.setItemMeta(itemMeta);
        bandage.setAmount(amount);
        return bandage;
    }
    public ItemStack getAdrenaline(int amount) {
        ItemStack adrenaline = new ItemStack(Material.BLAZE_ROD);
        ItemMeta itemMeta = adrenaline.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + bandageConfig.getConfig().getString("adrenalinename"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(bandageConfig.getConfig().getString("adrenalinelore"));
        itemMeta.setLore(lore);
        itemMeta.setCustomModelData(2);
        adrenaline.setItemMeta(itemMeta);
        adrenaline.setAmount(amount);
        return adrenaline;
    }

    @EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof Player) {
            boolean disableRegeneration = bandageConfig.getConfig().getBoolean("disableregeneration");
            if (disableRegeneration) { // if it is true

                if (!entity.hasPermission("bandageplugin.regeneration"))
                    event.setCancelled(true);
            } else
                return;
        }
        else
            return;
    }

    @EventHandler
    public void onPlayerClicks(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getItem() != null && item.isSimilar(getBandage(1))) {
                if((cooldown.get(player.getUniqueId().toString()) == null)){
                    player.playSound(player.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
                    if (player.getHealth() + bandageConfig.getConfig().getInt("healamount") > player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
                        wyleczmax(player);
                    else
                        player.setHealth(player.getHealth() + bandageConfig.getConfig().getInt("healamount"));
                    if(player.getInventory().getItemInOffHand().equals(item))
                        item.setAmount(item.getAmount() - 1);
                    else
                        player.getInventory().removeItem(getBandage(1));
                    cooldown.put(player.getUniqueId().toString(), (System.currentTimeMillis() / 1000));

                }
                else if((cooldown.get(player.getUniqueId().toString()) + bandageConfig.getConfig().getInt("cooldown")) <= (System.currentTimeMillis() / 1000))  {
                    player.playSound(player.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
                    if (player.getHealth() + bandageConfig.getConfig().getInt("healamount") > player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
                        wyleczmax(player);
                    else
                        player.setHealth(player.getHealth() + bandageConfig.getConfig().getInt("healamount"));
                    if(player.getInventory().getItemInOffHand().equals(item))
                        item.setAmount(item.getAmount() - 1);
                    else
                        player.getInventory().removeItem(getBandage(1));
                    cooldown.put(player.getUniqueId().toString(), (System.currentTimeMillis() / 1000));
                }
                else
                    player.sendMessage(ChatColor.GRAY + "Musisz odczekać jeszcze"
                            + ChatColor.RED + " "
                            + (cooldown.get(player.getUniqueId().toString())
                            + bandageConfig.getConfig().getInt("cooldown") - System.currentTimeMillis() / 1000) + "s" );
            }
            if (event.getItem() != null && item.isSimilar(getAdrenaline(1))){
                if(!Cooldown.checkPlayerCooldown(player, 120)){
                    player.playSound(player.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
                    if (player.getHealth() + bandageConfig.getConfig().getInt("adrenalinehealamount") > player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
                        wyleczmax(player);
                    else
                        player.setHealth(player.getHealth() + bandageConfig.getConfig().getInt("adrenalinehealamount"));
                    if(player.getInventory().getItemInOffHand().equals(item))
                        item.setAmount(item.getAmount() - 1);
                    else
                        player.getInventory().removeItem(getAdrenaline(1));
                    Cooldown.setSystemTime(player);
                }
            }
        }
    }
}
