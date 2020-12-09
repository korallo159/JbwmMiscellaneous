package koral.jbwmmiscellaneous.modules;

import koral.jbwmmiscellaneous.JbwmMiscellaneous;
import koral.jbwmmiscellaneous.database.StatsStatements;
import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import koral.jbwmmiscellaneous.modules.gui.StatsGui;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
@ModuleManager.Moduł
public class Stats extends CommandManager implements Listener {
    StatsGui statsGui = new StatsGui();
    private StatsStatements statsStatements = new StatsStatements();
    public Stats() {
        super("stats");
    }



    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("stats") && args.length == 1){
            List<String> commands = new ArrayList<>();
            commands.add("add");
            commands.add("remove");
            commands.add("advancedstatadd");
            return commands;
        }
        if (cmd.getName().equals("stats") && args[0].equals("add")) {
            return statistics();
        }
        if (cmd.getName().equals("stats") && args[0].equals("remove")){
            return statsStatements.getCurrentColumNames();
        }
        if(cmd.getName().equals("stats") && args[0].equals("advancedstatadd") && args.length ==2){
            return advancedStatistics();
        }
        if(cmd.getName().equals("stats") && args[0].equals("advancedstatadd") &&  args[1].equals("MINE_BLOCK") && args.length ==3 ){
            return statsMine_Block();
        }

        if(cmd.getName().equals("stats") && args[0].equals("advancedstatadd") &&  args[1].equals("USE_ITEM") || args[1].equals("BREAK_ITEM")  || args[1].equals("CRAFT_ITEM")
                ||args[1].equals("PICKUP") || args[1].equals("DROP") && args.length ==3 ){
            return statsITEMS();
        }
        if(cmd.getName().equals("stats") && args[0].equals("advancedstatadd") && args[1].equals("KILL_ENTITY") || args[1].equals("ENTITY_KILLED_BY") && args.length == 3){
            return statsENTITY();
        }
        return null;
    }

    public static List<String> advancedStatistics(){
        List<String> advstats = new ArrayList<>();
        advstats.add(Statistic.MINE_BLOCK.toString()); advstats.add(Statistic.USE_ITEM.toString()); advstats.add(Statistic.BREAK_ITEM.toString());
        advstats.add(Statistic.CRAFT_ITEM.toString()); advstats.add(Statistic.KILL_ENTITY.toString()); advstats.add(Statistic.PICKUP.toString());
        advstats.add(Statistic.DROP.toString()); advstats.add(Statistic.ENTITY_KILLED_BY.toString());
        return advstats;
    }
    public List<String> statsMine_Block(){
        List<String> list = new ArrayList<>();
        for(Material material: Material.values()){
            if(material.isBlock()){
                list.add(material.toString());
            }
        }
        return list;
    }

    public static List<String> statistics(){
        List<String> stats = new ArrayList<>();
        for (Statistic s : Statistic.values()) {
            stats.add(s.toString());
        }
        List<String> blackStatsList = new ArrayList<>();
        blackStatsList.add(Statistic.MINE_BLOCK.toString()); blackStatsList.add(Statistic.USE_ITEM.toString()); blackStatsList.add(Statistic.BREAK_ITEM.toString());
        blackStatsList.add(Statistic.CRAFT_ITEM.toString());blackStatsList.add(Statistic.KILL_ENTITY.toString()); blackStatsList.add(Statistic.ENTITY_KILLED_BY.toString());
        blackStatsList.add(Statistic.PICKUP.toString());
        blackStatsList.add(Statistic.DROP.toString());
        stats.removeAll(blackStatsList);
        return stats;
    }

    public List<String> statsITEMS(){
        List<String> list = new ArrayList<>();
        for(Material material: Material.values()){
            if(material.isItem()){
                list.add(material.toString());
            }
        }
        return list;
    }

    public static List<String> statsENTITY(){
        List<String> list = new ArrayList<>();
        for(EntityType entity: EntityType.values()){
            if(entity.isSpawnable() && entity.isAlive())
                list.add(entity.toString());
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if(cmd.getName().equals("statspurge") &&args.length > 0){
            OfflinePlayer target2 = Bukkit.getOfflinePlayer(args[0]);
            if(!target2.hasPlayedBefore()){
                p.sendMessage(ChatColor.RED + "Taki gracz nigdy tutaj nie gral!");
            }
            else {
                resetStats(target2);
                p.sendMessage(ChatColor.GREEN + "Wyczyszczono statystyki gracza: " + ChatColor.YELLOW + target2.getName());
            }
            return true;
        }
        if(cmd.getName().equals("stats") && args.length == 0){
            p.openInventory(statsGui.getInv());
            p.sendMessage("GUITest");
            return true;
        }

        if(cmd.getName().equals("stats") && args.length > 0)
            switch(args[0]){
                case "add":
                    if(args.length == 2){
                        Statistic s = Statistic.valueOf(String.valueOf((args[1])));
                        CustomStatAsyncCreate(s.toString());
                        p.sendMessage(ChatColor.GREEN + "Utworzono kolumne " + s.toString());
                    }
                    else
                        sender.sendMessage(ChatColor.RED + "poprawne użycie: /stats add <Statystyka>");
                    break;
                case"remove":
                    if(args.length == 2) {
                        if(args[1].equals("UUID") || args[1].equals("NICK")) {
                            sender.sendMessage(ChatColor.RED + "Nie możesz usunąć głównych kolumn, uszkodziłoby to tabelę.");
                            break;
                        }
                        ColumnRemoveAsync(args[1]);
                        p.sendMessage(ChatColor.RED + "Usunięto kolumnę: " + args[1]);
                    }
                    else
                        sender.sendMessage(ChatColor.RED + "poprawne użycie: /stats remove <nazwa_kolumny>");
                    break;
                case"advancedstatadd":
                    if(args.length == 3){
                        String s = args[1] + "x" + args[2];
                        if(args[1].equals("BREAK_ITEM") || args[1].equals("CRAFT_ITEM") || args[1].equals("ENTITY_KILLED_BY") || args[1].equals("PICKUP")
                                || args[1].equals("DROP") || args[1].equals("KILL_ENTITY") || args[1].equals("MINE_BLOCK") || args[1].equals("USE_ITEM")) {
                            CustomAdvancedStatisticAsyncCreate(s);
                            sender.sendMessage(ChatColor.GREEN + "Utworzono kolumnę" + s);
                        }
                        else sender.sendMessage("Niepoprawny pierwszy argument!");
                    }
                    else
                        sender.sendMessage(ChatColor.RED + "poprawne użycie: /stats advancedstatadd <NAZWA_STATYSTYKI> <TYP>");
                    break;
            }
        return true;
    }
    //TODO reset function
    public void resetStats(OfflinePlayer p){

    }




    public void CustomStatAsyncCreate(String s) {
        Bukkit.getScheduler().runTaskAsynchronously(JbwmMiscellaneous.getJbwmMiscellaneous(), () -> statsStatements.customStatisticCreate(s));
    }

    public void CustomAdvancedStatisticAsyncCreate(String s) {
        Bukkit.getScheduler().runTaskAsynchronously(JbwmMiscellaneous.getJbwmMiscellaneous(), () -> statsStatements.customAdvancedStatisticCreate(s));
    }

    public void ColumnRemoveAsync(String s) {
        Bukkit.getScheduler().runTaskAsynchronously(JbwmMiscellaneous.getJbwmMiscellaneous(), () -> statsStatements.statisticRemove(s));
    }


    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if(!event.getView().getTitle().equals(ChatColor.RED + "Gui stats manager")) return;
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        //Otwieranie kolejnych EQ z pierwszego eq.
        if(event.getClickedInventory().equals(statsGui.getInv())){
            switch(event.getCurrentItem().getType()){
                case BOOK:
                    player.openInventory(statsGui.pagestats1());
                    break;
                case BOOKSHELF:
                  //  player.openInventory(statsGui.pageAdvancedStats());
                    break;
                case WRITABLE_BOOK:
                    player.openInventory(statsGui.pageRemove());
                    break;
            }
        }
        //Create statistic inventory
        if(event.getClickedInventory().equals(statsGui.getInvStats1())){
            if(Stats.statistics().contains(event.getCurrentItem().getItemMeta().getDisplayName())) {
                statsStatements.customStatisticCreate(event.getCurrentItem().getItemMeta().getDisplayName());
                player.sendMessage(ChatColor.GREEN + "Created column: " + event.getCurrentItem().getItemMeta().getDisplayName());
            }
            if(event.getCurrentItem().getType().equals(Material.KELP)){
                player.openInventory(statsGui.pagestats2());
        }
      }
        //Create statistic inventory 2
        if(event.getClickedInventory().equals(statsGui.getInvStats2())){
            if(Stats.statistics().contains(event.getCurrentItem().getItemMeta().getDisplayName())) {
                statsStatements.customStatisticCreate(event.getCurrentItem().getItemMeta().getDisplayName());
                player.sendMessage(ChatColor.GREEN + "Created column: " + event.getCurrentItem().getItemMeta().getDisplayName());
            }
        }
        if(event.getClickedInventory().equals(statsGui.getInvStatsAdvanced())){
            if(Stats.advancedStatistics().contains(event.getCurrentItem().getItemMeta().getDisplayName())){
               String s1 = event.getCurrentItem().getItemMeta().getDisplayName();
                    if(s1.equals("KILL_ENTITY") || s1.equals("ENTITY_KILLED_BY"))
                        player.openInventory(statsGui.pageAdvancedEntity());
                    if(s1.equals("USE_ITEM") || s1.equals("BREAK_ITEM") || s1.equals("CRAFT_ITEM"));

            }
        }

        //Remove statistic inventory
        if (event.getClickedInventory().equals(statsGui.getInvRemove())) {
            if(statsStatements.getCurrentColumNames().contains(event.getCurrentItem().getItemMeta().getDisplayName())) {
                statsStatements.statisticRemove(event.getCurrentItem().getItemMeta().getDisplayName());
                player.sendMessage(ChatColor.RED + "Deleted column: " + event.getCurrentItem().getItemMeta().getDisplayName());
                player.openInventory(statsGui.getInv());
            }
        }
        if(event.getCurrentItem().getType().equals(Material.BARRIER)){
            player.openInventory(statsGui.getInv());
        }
        event.setCancelled(true);
    }


}

