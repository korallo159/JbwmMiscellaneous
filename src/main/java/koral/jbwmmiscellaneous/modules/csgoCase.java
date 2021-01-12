package koral.jbwmmiscellaneous.modules;

import koral.jbwmmiscellaneous.JbwmMiscellaneous;
import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ConfigManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import koral.jbwmmiscellaneous.util.Cooldowns;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@ModuleManager.Moduł
public class csgoCase extends CommandManager implements Listener {
    public csgoCase() {
        super("case", "/case <argument>");
    }

    HashMap<Player, Inventory> inventoryHashMap = new HashMap<>();
    HashMap<Player, BukkitTask> taskHashMap = new HashMap<>();
    HashMap<Player, BukkitTask> globalTaskHashMap = new HashMap<>();
    HashMap<Player, Integer> winInteger = new HashMap<>();
    ConfigManager csgoCaseConfig = new ConfigManager("csgoCase.yml");
    Cooldowns cooldown = new Cooldowns(new HashMap<>());

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return utab(args, "testopening", "reload", "add");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0 ) return false;
        switch(args[0]){
            case"reload":
                csgoCaseConfig.reloadCustomConfig();
                sender.sendMessage(ChatColor.GREEN + "Przeładowałeś case config");
                break;
            case"testopening":
                winInteger.put((Player) sender, winItem());
                runAnimation((Player) sender);
                break;
            case"add":
                Player target = Bukkit.getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Taki gracz nie jest online!");
                    return true;
                }
                else{
                    target.getInventory().addItem(caseItem(Integer.valueOf(args[2])));
                }
        }
        return true;
    }

    //od 10 do 16
    private void runAnimation(Player player) {
        inventoryHashMap.put(player, Bukkit.getServer().createInventory(null, 27, "§4§lSkrzynia z skinami"));
        setStartInv(player);
        player.openInventory(inventoryHashMap.get(player));
        BukkitTask task = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i == 0)
                    runRolling(2, player);

                i++;

                if (i == 3) {
                    taskHashMap.get(player).cancel();
                    runRolling(5, player);
                }

                if (i == 5) {
                    taskHashMap.get(player).cancel();
                    runRolling(10, player);
                }

                if (i == 7) {
                    taskHashMap.get(player).cancel();
                    runRolling(20, player);
                }
                if (i == 8) {
                    taskHashMap.get(player).cancel();
                    moveItemsToLeftandRollWinItem(player);
                }
                if(i == 9){
                    runRolling(20, player);
                }
                if(i == 12){
                    taskHashMap.get(player).cancel();
                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1.0F, 1.0F);
                }
                if (i == 13) {
                    cancel();
                }

            }
        }.runTaskTimerAsynchronously(JbwmMiscellaneous.getJbwmMiscellaneous(), 0, 20);
        globalTaskHashMap.put(player, task);
    }

    private ArrayList<ItemStack> itemStackList() {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        for (String skins : csgoCaseConfig.getConfig().getConfigurationSection("skiny").getKeys(false)) {
            itemStacks.add(csgoCaseConfig.getConfig().getItemStack("skiny." + skins + ".item"));

        }
        return itemStacks;

    }


    private void setStartInv(Player player) {
            ItemStack dark = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta itemMeta = dark.getItemMeta();
            itemMeta.setDisplayName("§0§lX");
            dark.setItemMeta(itemMeta);
        for (int i = 0; i < 27; i++) {
             if(i == 0 || i ==9 || i ==18 || i == 8 || i == 17 || i ==22 || i ==26 )
                 inventoryHashMap.get(player).setItem(i, dark);
        }
        ItemStack itemStack = new ItemStack(Material.END_CRYSTAL);
        ItemMeta itemMeta2 = itemStack.getItemMeta();
        itemMeta2.setDisplayName("§a§lWYGRANA");
        itemStack.setItemMeta(itemMeta2);
        inventoryHashMap.get(player).setItem(4, itemStack);
        inventoryHashMap.get(player).setItem(22, itemStack);
    }

    private void moveItemsToLeftandRollNextItem(Player player) {
        Random random = new Random();
        for (int i = 10; i < 16; i++) {
            //przesuwanie skina
            if(i!=12) {
                //przesuwanie skina
                if(inventoryHashMap.containsKey(player))
                inventoryHashMap.get(player).setItem(i, inventoryHashMap.get(player).getItem(i + 1));
                else return;
                //przesuwanie paneli
                if(i!=13) {
                    inventoryHashMap.get(player).setItem(i + 9, inventoryHashMap.get(player).getItem(i + 10));
                    inventoryHashMap.get(player).setItem(i - 9, inventoryHashMap.get(player).getItem(i - 8));
                }
            }
           else if(i==12){
                inventoryHashMap.get(player).setItem(i, inventoryHashMap.get(player).getItem(i +1));
                ItemStack itemStack = inventoryHashMap.get(player).getItem(i +1);
                if(itemStack != null)
                createColorPanelsUpAndDown(itemStack, player, 3, 21);

            }

            if (i == 15) {
                ItemStack losowy = itemStackList().get(random.nextInt(getSkinsSize()));
                inventoryHashMap.get(player).setItem(16, losowy);
                createColorPanelsUpAndDown(losowy, player, 7, 25);
            }
        }
    }

    private void createColorPanelsUpAndDown(ItemStack itemStack, Player player, int up, int down){
        String nazwa = itemStack.getItemMeta().getDisplayName();
        //light_blue
        if(nazwa.contains("§b")){
            inventoryHashMap.get(player).setItem(up, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
            inventoryHashMap.get(player).setItem(down, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
        }
        //blue
        else if(nazwa.contains("§1")){
            inventoryHashMap.get(player).setItem(up, new ItemStack(Material.BLUE_STAINED_GLASS_PANE));
            inventoryHashMap.get(player).setItem(down, new ItemStack(Material.BLUE_STAINED_GLASS_PANE));
        }
        //purple
        else if(nazwa.contains("§5")){
            inventoryHashMap.get(player).setItem(up, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE));
            inventoryHashMap.get(player).setItem(down, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE));
        }
        //rozowy
        else if(nazwa.contains("§d")){
            inventoryHashMap.get(player).setItem(up, new ItemStack(Material.PINK_STAINED_GLASS_PANE));
            inventoryHashMap.get(player).setItem(down, new ItemStack(Material.PINK_STAINED_GLASS_PANE));
        }
        //czerwony
        else if(nazwa.contains("§4")){
            inventoryHashMap.get(player).setItem(up, new ItemStack(Material.RED_STAINED_GLASS_PANE));
            inventoryHashMap.get(player).setItem(down, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        }
        //zloty
        else if(nazwa.contains("§6")){
            inventoryHashMap.get(player).setItem(up, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE));
            inventoryHashMap.get(player).setItem(down, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE));
        }
        else{//7 25

        }

    }
//TODO: JEZELI JEST NA SLOCIE 13+9 albo 13-9 TO WTEDY NAD i POD ROBI ZIELONY.
    private void moveItemsToLeftandRollWinItem(Player player) {
        for (int i = 10; i < 16; i++) {
            if(i!=12) {
                //przesuwanie skina
                inventoryHashMap.get(player).setItem(i, inventoryHashMap.get(player).getItem(i + 1));
                //przesuwanie paneli
                if(i!=13) {
                    inventoryHashMap.get(player).setItem(i + 9, inventoryHashMap.get(player).getItem(i + 10));
                    inventoryHashMap.get(player).setItem(i - 9, inventoryHashMap.get(player).getItem(i - 8));
                }
            }
            else if(i==12){
                inventoryHashMap.get(player).setItem(i, inventoryHashMap.get(player).getItem(i +1));
                ItemStack itemStack = inventoryHashMap.get(player).getItem(i +1);
                if(itemStack != null)
                    createColorPanelsUpAndDown(itemStack, player, 3, 21);

            }
            if (i == 15) {
                ItemStack wygrana = itemStackList().get(winInteger.get(player) - 1);
                inventoryHashMap.get(player).setItem(16, wygrana);
                createColorPanelsUpAndDown(wygrana, player, 7, 25);
            }
        }
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
    }


    private void runRolling(long howFast, Player player) {
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                moveItemsToLeftandRollNextItem(player);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
            }
        }.runTaskTimerAsynchronously(JbwmMiscellaneous.getJbwmMiscellaneous(), 0, howFast);
        taskHashMap.put(player, task);
    }

    private Integer getSkinsSize() {
        int i = 0;
        for (String skins : csgoCaseConfig.getConfig().getConfigurationSection("skiny").getKeys(false)) {
            i++;
        }
        return i;
    }

    private Integer getPula() {
        int calyZakres = 0;
        for(String skins: csgoCaseConfig.getConfig().getConfigurationSection("skiny").getKeys(false)) {
            calyZakres = calyZakres + csgoCaseConfig.getConfig().getInt("skiny." + skins + ".pula");
        }
        return calyZakres;
    }

    //TODO: odpalac te obliczenia na osobnym watku, mozliwe ze nie trzeba bo odpala sie wiekszosc na osobnym.
    private Integer winItem() {
        Random random = new Random();
        int winItem = 0;
        int x = 1; //min
        int y = getPula(); // max
        int losowaZPuli = random.nextInt(y - x + 1) + x;
        int j = 0;
        outerloop:
        for (String skins : csgoCaseConfig.getConfig().getConfigurationSection("skiny").getKeys(false)) {
            for(int i = 0; i< csgoCaseConfig.getConfig().getInt("skiny." + skins + ".pula"); i++){
                j++;
                if(j == losowaZPuli) {
                    winItem = Integer.valueOf(skins);
                    break outerloop;
                }

            }
        }
        return winItem;
    }

    private ItemStack caseItem(int amount){
        ItemStack itemStack = new ItemStack(Material.CHEST);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Skrzynia ze skinami");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Kliknij prawym, aby otworzyć skrzynię ze skinami");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        itemStack.setAmount(amount);
        return itemStack;
    }

    @EventHandler
    public void onplayerCloseInv(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventoryHashMap.get(event.getPlayer()))) {
            globalTaskHashMap.get(event.getPlayer()).cancel();
            globalTaskHashMap.remove(event.getPlayer());
            taskHashMap.get(event.getPlayer()).cancel();
            taskHashMap.remove(event.getPlayer());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"lp user " + event.getPlayer().getName() + " permission set " +
                    csgoCaseConfig.getConfig().getString("skiny." + winInteger.get(event.getPlayer()) + ".permisja"));
            Bukkit.broadcastMessage("§2[§aSkiny§2]§f " +((Player) event.getPlayer()).getDisplayName() + " §7§lwylosował skin " + csgoCaseConfig.getConfig().getItemStack(
                    "skiny." + winInteger.get(event.getPlayer()) + ".item").getItemMeta().getDisplayName());
            winInteger.remove(event.getPlayer());
            inventoryHashMap.remove(event.getPlayer());
        }
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event){
        if(!event.getView().getTitle().equals("§4§lSkrzynia z skinami")) return;
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerClicks(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        if (action.equals(Action.RIGHT_CLICK_AIR)) {
            if (event.getItem() != null && item.getItemMeta().getLore() != null && item.isSimilar(caseItem(1))) {
                    if (!cooldown.checkPlayerCooldown(player, 5)) {
                        for (int i = 0; i < 100; i++) {
                            winInteger.put(player, winItem());
                            if (!player.hasPermission(csgoCaseConfig.getConfig().getString("skiny." + winInteger.get(player) + ".permisja"))) {
                                runAnimation(player);
                                player.getInventory().removeItemAnySlot(caseItem(1));
                                cooldown.setSystemTime(player);
                                break;
                            }
                            if (i == 99) {
                                player.sendMessage(ChatColor.RED + "skrzynie się przegrzały, poczekaj chwilę");
                                cooldown.setSystemTime(player);
                            }
                        }
                    }
                    return;

            }
        }
    }

    @EventHandler
    public void playerPlaceBlockEvent(BlockPlaceEvent event){
        if(event.getItemInHand().isSimilar(caseItem(1))){
            event.getPlayer().sendMessage(ChatColor.RED + "Aby otworzyć skrzynie, kliknij nią w powietrze");
            event.setCancelled(true);
        }
    }


//TODO: dynamiczne panele, zeby wygladalo czy jest niebieski skin etc.

}


// szukanie ktora liczba lezy blizej ktorej na podstawie configu
 /*   public void countChanceItems(){
        HashMap<Integer, Integer> skinSzansaHashMap = new HashMap<>();
        int calyZakres = 0;
        for(String skins: csgoCaseConfig.getConfig().getConfigurationSection("skiny").getKeys(false)){
            calyZakres = calyZakres + csgoCaseConfig.getConfig().getInt("skiny." + skins + ".zakres");
            skinSzansaHashMap.put(Integer.valueOf(skins), csgoCaseConfig.getConfig().getInt("skiny." + skins + ".zakres"));
        }
        List<Integer> indexes = new ArrayList<Integer>(skinSzansaHashMap.keySet()); // <== Set to List

        int x = 0; //min
        int y = calyZakres; // max
        Random random = new Random();
        int losowa = random.nextInt(y - x + 1) + x;
        int distance = Math.abs(skinSzansaHashMap.get(skinSzansaHashMap.keySet().toArray()[0]) - losowa);
        int idx = 0;
        for(int i = 1; i < skinSzansaHashMap.size(); i++){
            int cdistance = Math.abs(skinSzansaHashMap.get(skinSzansaHashMap.keySet().toArray()[i]) - losowa);
            if(cdistance < distance){
                idx = i;
                distance = cdistance;
            }
        }
        JbwmMiscellaneous.log(calyZakres);
        JbwmMiscellaneous.log("Losowa liczba z calego zakresu " + losowa);
        JbwmMiscellaneous.log("Wygrywa skin z numerem: " + indexes.get(idx));
    }

  */