package koral.jbwmmiscellaneous.modules.gui;

import koral.jbwmmiscellaneous.database.StatsStatements;
import koral.jbwmmiscellaneous.modules.Stats;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class StatsGui {
    static Inventory inv;
    Inventory invStats1;
    Inventory invStats2;
    Inventory invStatsAdvanced;
    Inventory invRemove;

    public Inventory getInv() {
        return inv;
    }

    public Inventory getInvStats1() {
        return invStats1;
    }

    public Inventory getInvStats2() {
        return invStats2;
    }

    public Inventory getInvStatsAdvanced() {
        return invStatsAdvanced;
    }

    public Inventory getInvRemove() {
        return invRemove;
    }

    static {
        inv = Bukkit.createInventory(null, 54, ChatColor.RED + "Gui stats manager");
        inv.setItem(3, createItem(Material.BOOK, ChatColor.RED +"Create statistic", ChatColor.GRAY+ "Click if you want create statistic"));
        inv.setItem(4, createItem(Material.BOOKSHELF, ChatColor.RED+ "Create advanced statistic", ChatColor.GRAY+ "Aktualnie dostępne tylko przez komende /stats"));
        inv.setItem(5, createItem(Material.WRITABLE_BOOK, ChatColor.RED +"Delete column from database", ChatColor.GRAY+ "Click if you want delete column from MYSQL"));
        inv.setItem(53, createItem(Material.BARRIER, "Powrót", "Kliknij aby powrócić"));
    }

    private static ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);
        return item;
    }

    public Inventory pagestats1() {
        invStats1 = Bukkit.createInventory(null, 54, ChatColor.RED + "Gui stats manager");
        for(int i =0; i<52; i++){
            invStats1.setItem(i, createItem(Material.WRITABLE_BOOK, Stats.statistics().get(i)));
        }
        invStats1.setItem(52,createItem(Material.KELP, "Page 2"));
        invStats1.setItem(53, createItem(Material.BARRIER, "Powrót"));
        return invStats1;
    }
    public Inventory pagestats2() {
        invStats2 = Bukkit.createInventory(null, 54, ChatColor.RED + "Gui stats manager");
        for(int i =0; i<52; i++){
            int b = i + 52;
            if(b==74) break;
            invStats2.setItem(i, createItem(Material.WRITABLE_BOOK, Stats.statistics().get(b)));
        }
        invStats2.setItem(53, createItem(Material.BARRIER, "Powrót"));
        return invStats2;
    }

    public Inventory pageAdvancedStats() {
        invStatsAdvanced = Bukkit.createInventory(null, 54, ChatColor.RED + "Gui stats manager");
        for(int i =0; i<52; i++){
            if(Stats.advancedStatistics().size() > i) {
                invStatsAdvanced.setItem(i, createItem(Material.BOOKSHELF, Stats.advancedStatistics().get(i)));
            }
            else break;
        }
        invStatsAdvanced.setItem(53, createItem(Material.BARRIER, "Powrót"));
        return invStatsAdvanced;
    }

    public Inventory pageAdvancedEntity() {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.RED + "Gui stats manager");
        for(int i =0; i<52; i++){
            if(Stats.statsENTITY().size() > i) {
                inv.setItem(i, createItem(Material.BOOKSHELF, Stats.statsENTITY().get(i)));
            }
            else break;
        }
        inv.setItem(52, createItem(Material.KELP, "Page 2"));
        inv.setItem(53, createItem(Material.BARRIER, "Powrót"));
        return inv;
    }

    public Inventory pageRemove(){
        invRemove = Bukkit.createInventory(null, 54, ChatColor.RED + "Gui stats manager");
        for(int i=0; i<52; i++){
            if(StatsStatements.getCurrentColumNames().size() > i) {
                invRemove.setItem(i, createItem(Material.BOOK, StatsStatements.getCurrentColumNames().get(i)));
            }
            else break;
        }
        invRemove.setItem(53, createItem(Material.BARRIER, "Powrót"));
        return invRemove;
    }


}
