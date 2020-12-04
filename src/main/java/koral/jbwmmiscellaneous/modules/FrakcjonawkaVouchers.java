package koral.jbwmmiscellaneous.modules;

import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
@ModuleManager.Moduł
public class FrakcjonawkaVouchers extends CommandManager implements Listener {

    public FrakcjonawkaVouchers() {
        super("dajvoucher");
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("dajvoucher")) {
                {
                    if (args.length == 0) {
                        player.getInventory().addItem(getItem());
                        return true;
                    } else {
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
                }
            }
            if (cmd.getName().equalsIgnoreCase("voucherinfoadmin")) {
                player.sendMessage(ChatColor.DARK_GRAY + " ");
                player.sendMessage(ChatColor.RED + "Info do adminów, jakby kiedyś grzebali przy nazwach rang i nie działało");
                player.sendMessage(ChatColor.DARK_GRAY + "__________________________________________________");
                player.sendMessage(ChatColor.DARK_GRAY + " ");
                player.sendMessage(ChatColor.YELLOW + "1." + ChatColor.GRAY + "Plugin po dodaniu vouchera otwiera gui gdzie wybieramy opcję");
                player.sendMessage(ChatColor.YELLOW + "2." + ChatColor.RED + "Plugin zabiera 2 permisje i dodaje jedną analogicznie jak przy portalach.");
                player.sendMessage(ChatColor.YELLOW + "3." + ChatColor.GRAY + "Plugin operuje na rangach KOM, AKAP, NAZI i ustawia przez luck perms");

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
                player.sendMessage(ChatColor.RED + "Zużyleś voucher na zmianę frakcji");
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

    // Cancel dragging in our inventory


}
