package koral.jbwmmiscellaneous.modules;

import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ConfigManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ModuleManager.Moduł
public class Frakcjonawka extends CommandManager implements Listener {

    private boolean freeze = false;
    ConfigManager animalConfig = new ConfigManager("animalToEgg.yml");

    public Frakcjonawka() {
        super("chat");
        ustawKomende("dajvoucher", "/dajvoucher <nick>", Collections.emptyList());
        ustawKomende("animaltransformer", "/animaltransformer - daje item do reki", Collections.emptyList());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equals("chat"))
        return utab(args, "freeze");

        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equals("chat") && args.length > 0)
                switch (args[0]) {
                    case "freeze":
                        if (!freeze) {
                            freeze = true;
                            sender.sendMessage(ChatColor.RED + "Czat został zfreezowany");
                        } else {
                            freeze = false;
                            sender.sendMessage(ChatColor.GREEN + "Czat został odfreezowany");
                        }
                        break;
                }
            if (cmd.getName().equalsIgnoreCase("dajvoucher")) {
                {
                    if (args.length == 0) {
                        player.getInventory().addItem(getItem());
                    } else {
                        final Player target = Bukkit.getServer().getPlayer(args[0]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.RED + "Taki gracz nie jest online!");
                            return true;
                        } else
                            target.getInventory().addItem(getItem());
                        sender.sendMessage(ChatColor.GRAY + "Gracz " + ChatColor.YELLOW + args[0] + ChatColor.GRAY + " dostał od Ciebie voucher na zmianę frakcji.");
                        target.sendMessage(ChatColor.GRAY + "Otrzymałeś Voucher na zmianę frakcji, kliknij na niego " + ChatColor.YELLOW + "PPM" + ChatColor.GRAY + " aby przejść do menu zmiany frakcji");
                    }
                    return true;
                }
            }
            if(cmd.getName().equals("animaltransformer") && args.length == 0) {
                ((Player) sender).getInventory().addItem(addStick());
                return true;
            }

            if(cmd.getName().equals("animaltransformer") && args[0].equals("reload")){
                animalConfig.reloadCustomConfig();
                sender.sendMessage(ChatColor.RED + "Config mobkow przeladowany");
                return true;
            }

        }
        else if (sender instanceof ConsoleCommandSender)
        {
            if (cmd.getName().equalsIgnoreCase("dajvoucher") || cmd.getName().equalsIgnoreCase("voucherinfoadmin")){
                sender.sendMessage("komenda niedostepna dla konsoli");
                return true;
            }

            final Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Taki gracz nie jest online!");
                return true;
            } else
                target.getInventory().addItem(getItem());
            sender.sendMessage(ChatColor.GRAY + "Gracz " + ChatColor.YELLOW + args[0] + ChatColor.GRAY + " dostał od Ciebie voucher na zmianę frakcji.");
            target.sendMessage(ChatColor.GRAY + "Otrzymałeś Voucher na zmianę frakcji, kliknij na niego " + ChatColor.YELLOW + "PPM" + ChatColor.GRAY + " aby przejść do menu zmiany frakcji");
            return true;
        }
        else if(sender instanceof RemoteConsoleCommandSender)
        {
            if (cmd.getName().equalsIgnoreCase("dajvoucher") || cmd.getName().equalsIgnoreCase("voucherinfoadmin")){
                return true;
            }

            final Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null) {
                return true;
            } else
                target.getInventory().addItem(getItem());
            target.sendMessage(ChatColor.GRAY + "Otrzymałeś Voucher na zmianę frakcji, kliknij na niego " + ChatColor.YELLOW + "PPM" + ChatColor.GRAY + " aby przejść do menu zmiany frakcji");
            return true;
        }
        return true;
    }

    @EventHandler
    public void onAsyncplayerChatEvent(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        if(freeze){
            if(!p.hasPermission("JbwmMiscellaneous.admin.chat")) {
                p.sendMessage(ChatColor.RED + "Czat został wyłączony przez administratora, nie możesz wysyłać wiadomości.");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractAtEntityEvent e) {
        if(e.getPlayer().hasPermission("JbwmMiscellaneous.animaltransformer.transform")){
            if(animalConfig.config.getStringList("mobs").contains(e.getRightClicked().getName().toUpperCase())) {
                EquipmentSlot equipmentSlot = e.getHand();
                if (equipmentSlot.equals(EquipmentSlot.HAND)) {
                    if(e.getPlayer().getInventory().getItemInMainHand().isSimilar(addStick())) {
                        Player player = e.getPlayer();
                        ItemStack jajco = new ItemStack(Material.valueOf(e.getRightClicked().getName().toUpperCase() + "_SPAWN_EGG"));
                        player.getInventory().addItem(jajco);
                        player.getInventory().removeItemAnySlot(addStick());
                        e.getRightClicked().remove();
                        player.spawnParticle(Particle.CLOUD, e.getRightClicked().getLocation(), 5);
                    }
                }
            }
        }
        else return;
    }


    @EventHandler
    public void onPlayerClicks(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();


        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {





            if (event.getItem() != null && item.getItemMeta().getLore() != null && item.getItemMeta().getLore().contains("Kliknij prawym, aby zmienić frakcję")) {
                player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_SHOOT, 1, 1);
                Inventory gui = Bukkit.createInventory(player, 9, ChatColor.DARK_RED + "Wybór frakcji");
                ItemStack kom =  new ItemStack (Material.POTATO);
                ItemStack nazi = new ItemStack (Material.WITHER_ROSE);
                ItemStack akap = new ItemStack (Material.DIAMOND);

                ItemMeta komMeta = kom.getItemMeta();
                ItemMeta naziMeta = kom.getItemMeta();
                ItemMeta akapMeta = kom.getItemMeta();

                komMeta.setDisplayName(ChatColor.GRAY +"Zmień frakcję na" + ChatColor.DARK_RED + " KOMUNĘ");
                naziMeta.setDisplayName(ChatColor.GRAY +"Zmień frakcję na" + ChatColor.DARK_GRAY + " NAZI");
                akapMeta.setDisplayName(ChatColor.GRAY + "Zmień frakcję na" + ChatColor.GOLD + " AKAP");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.DARK_RED + "Kliknij, aby zmienić frakcję");
                akapMeta.setLore(lore);
                komMeta.setLore(lore);
                naziMeta.setLore(lore);
                kom.setItemMeta(komMeta);
                nazi.setItemMeta(naziMeta);
                akap.setItemMeta(akapMeta);


                gui.setItem(2, kom);
                gui.setItem(4, nazi);
                gui.setItem(6, akap);
                player.openInventory(gui);
            }


        }
    }
    @EventHandler
    public void clickEvent(InventoryClickEvent e)
    {
        if(e.getView().getTitle().equals(ChatColor.DARK_RED + "Wybór frakcji"))
        {
            Player player = (Player) e.getWhoClicked();
            final ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            switch(e.getCurrentItem().getType()) {
                case POTATO:
                    player.closeInventory();
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user "+player.getName()+" parent add kom");
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent remove akap" );
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent remove nazi" );
                    player.sendMessage(ChatColor.GRAY + "Zmieniłeś frakcję na " + ChatColor.DARK_RED+ "KOM");
                    player.getInventory().removeItem(getItem());
                    break;
                case WITHER_ROSE:
                    player.closeInventory();
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user "+player.getName()+" parent add nazi");
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent remove kom" );
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent remove akap" );
                    player.sendMessage(ChatColor.GRAY + "Zmieniłeś frakcję na " + ChatColor.DARK_GRAY+ "NAZI");
                    player.getInventory().removeItem(getItem());
                    break;
                case DIAMOND:
                    player.closeInventory();
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user "+player.getName()+" parent add akap");
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent remove nazi" );
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent remove kom" );
                    player.sendMessage(ChatColor.GRAY + "Zmieniłeś frakcję na " + ChatColor.GOLD + "AKAP");
                    player.getInventory().removeItem(getItem());
                    break;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent e){

    }


    public ItemStack getItem() {
        ItemStack zmianafrakcji = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = zmianafrakcji.getItemMeta();

        itemMeta.setDisplayName(ChatColor.RED + "Voucher na przeniesienie do innej frakcji");

        ArrayList<String> lore = new ArrayList<>();
        lore.add("Kliknij prawym, aby zmienić frakcję");
        itemMeta.setLore(lore);
        zmianafrakcji.setItemMeta(itemMeta);

        return zmianafrakcji;
    }

    public ItemStack addStick(){
        ItemStack itemStack = new ItemStack(Material.GLASS_BOTTLE);
        ItemMeta itemStackItemMeta = itemStack.getItemMeta();
        itemStackItemMeta.setDisplayName(ChatColor.YELLOW + "Magiczna butla");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED +"Kliknij prawym na moba, aby zmienić go w jajko");
        itemStackItemMeta.setLore(lore);
        itemStack.setItemMeta(itemStackItemMeta);

        return itemStack;
    }



}
