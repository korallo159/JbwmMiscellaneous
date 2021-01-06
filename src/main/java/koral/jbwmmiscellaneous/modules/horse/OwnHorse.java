package koral.jbwmmiscellaneous.modules.horse;

import koral.jbwmmiscellaneous.JbwmMiscellaneous;
import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ConfigManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import koral.jbwmmiscellaneous.util.Cooldowns;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@ModuleManager.Moduł
public class OwnHorse extends CommandManager implements Listener {
    public static ConfigManager horseConfig = new ConfigManager("OwnHorse.yml");
    private Cooldowns cooldown = new Cooldowns(new HashMap<>());
    private HorseSpawner summonHorse = new HorseSpawner();
    public OwnHorse() {
        super("horse", "/horse mvip/svip spawnuje twojego konia", "kon", "konik", "wierzchowiec");
        ustawKomende("ksiegikoni", "/horseksiegi dodaje wszystkie ksiegi koni", Collections.emptyList());
        JbwmMiscellaneous.addPerm("JbwmMiscellaneous.horse.svip");
        JbwmMiscellaneous.addPerm("JbwmMiscellaneous.horse.mvip");
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player =(Player) sender;
            if(cmd.getName().equalsIgnoreCase("horse") && args.length == 0) {
                if (player.hasPermission("JbwmMiscellaneous.horse.svip")) {
                    if (!cooldown.checkPlayerCooldown(player, 30)) {
                        summonHorse.checkHorsePlayer(player);
                        summonHorse.SummonSkeletonPlayerHorse(player);
                        cooldown.setSystemTime(player, 0);
                    }
                } else if (player.hasPermission("JbwmMiscellaneous.horse.mvip")) {
                    if (!cooldown.checkPlayerCooldown(player, 30)) {
                        summonHorse.checkHorsePlayer(player);
                        summonHorse.SummonZombiePlayerHorse(player);
                        cooldown.setSystemTime(player, 0);
                    }
                }
                return true;
            }
            if(cmd.getName().equalsIgnoreCase("horse") && args[0].equalsIgnoreCase("reload")){
                horseConfig.reloadCustomConfig();
                player.sendMessage("przeladowano config");
                return true;
            }
         if(cmd.getName().equals("ksiegikoni")){
             player.getInventory().addItem(getHorseBook1());
             player.getInventory().addItem(getHorseBook2());
             player.getInventory().addItem(getHorseBook3());
             return true;
         }
        }
        return true;
    }

    @EventHandler
    public void onHorseTag(PlayerInteractEntityEvent e){
        Player p = e.getPlayer();
        if (p.getInventory().getItemInMainHand().getType().equals(Material.NAME_TAG)){
            if(e.getRightClicked().getType().equals(EntityType.HORSE) || e.getRightClicked().getType().equals(EntityType.SKELETON_HORSE) || e.getRightClicked().getType().equals(EntityType.ZOMBIE_HORSE)){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent e){
        if(e.getInventory() instanceof HorseInventory){
            HorseInventory i = (HorseInventory) e.getInventory();
            Horse h = (Horse) i.getHolder();
            Player p = (Player) e.getPlayer();
            if(!h.getOwner().getName().equals(p.getName()) && h.getOwner() != null) {
                e.setCancelled(true);
            }
        }
        if(e.getInventory() instanceof AbstractHorseInventory){
            AbstractHorseInventory i = (AbstractHorseInventory) e.getInventory();
            AbstractHorse h = (AbstractHorse) i.getHolder();
            Player p = (Player) e.getPlayer();
            if(!h.getOwner().getName().equals(p.getName()) && h.getOwner() != null) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHorseInvClickEvent(InventoryClickEvent e){
        if(e.getInventory() instanceof  HorseInventory){
            if(e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.SADDLE))
                e.setCancelled(true);
        }
        if(e.getInventory() instanceof  AbstractHorseInventory){
            if(e.getCurrentItem() != null &&  e.getCurrentItem().getType().equals(Material.SADDLE))
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHorseEnter(VehicleEnterEvent e){
        Entity h = e.getVehicle();
        if(h instanceof Horse && ((Horse) h).getOwner() != null) {
            Player p = (Player) e.getEntered();
            if (!((Horse) h).getOwner().getName().equals(p.getName())) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + "To nie twoj kon, nie mozesz na nim jezdzic!");
            }
        }
        else if (h instanceof AbstractHorse && ((AbstractHorse) h).getOwner() != null){
            Player p = (Player) e.getEntered();
            if (!((AbstractHorse) h).getOwner().getName().equals(p.getName())) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + "To nie twoj kon, nie mozesz na nim jezdzic!");
            }
        }

    }

    @EventHandler
    public void onPlayerPPM(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();


        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
            if (event.getItem() != null && item.getItemMeta().getLore() != null && item.getItemMeta().getLore().contains(ChatColor.RED + "Kliknij prawym aby przywołać wierzchowca klasy 1")){
                if(!cooldown.checkPlayerCooldown(player, 30)) {
                    summonHorse.checkHorsePlayer(player);
                    summonHorse.SummonDefaultPlayerHorse(player);
                    cooldown.setSystemTime(player, 0);
                }
            }
            if (event.getItem() != null && item.getItemMeta().getLore() != null && item.getItemMeta().getLore().contains(ChatColor.RED + "Kliknij prawym aby przywołać wierzchowca klasy 2")){
                if(!cooldown.checkPlayerCooldown(player, 30)) {
                    summonHorse.checkHorsePlayer(player);
                    summonHorse.SummonBattlePlayerHorse(player);
                    cooldown.setSystemTime(player, 0);
                }
            }

            if (event.getItem() != null && item.getItemMeta().getLore() != null && item.getItemMeta().getLore().contains(ChatColor.RED + "Kliknij prawym aby przywołać wierzchowca klasy 3")){
                if(!cooldown.checkPlayerCooldown(player, 30)) {
                    summonHorse.checkHorsePlayer(player);
                    summonHorse.SummonMilitaryPlayerHorse(player);
                    cooldown.setSystemTime(player, 0);
                }
            }
        }
    }

    public ItemStack getHorseBook1() {
        ItemStack horsebook = new ItemStack(Material.SADDLE);
        ItemMeta itemMeta = horsebook.getItemMeta();

        itemMeta.setDisplayName(ChatColor.RED + "Przywołanie wierzchowca klasy I");

        ArrayList<String> lore = new ArrayList<String>();
        lore.add((ChatColor.RED + "Kliknij prawym aby przywołać wierzchowca klasy 1"));
        itemMeta.setLore(lore);
        horsebook.setItemMeta(itemMeta);

        return horsebook;
    }

    public ItemStack getHorseBook2() {
        ItemStack horsebook = new ItemStack(Material.SADDLE);
        ItemMeta itemMeta = horsebook.getItemMeta();

        itemMeta.setDisplayName(ChatColor.RED + "Przywołanie wierzchowca klasy II");

        ArrayList<String> lore = new ArrayList<String>();
        lore.add((ChatColor.RED + "Kliknij prawym aby przywołać wierzchowca klasy 2"));
        itemMeta.setLore(lore);
        horsebook.setItemMeta(itemMeta);

        return horsebook;
    }

    public ItemStack getHorseBook3() {
        ItemStack horsebook = new ItemStack(Material.SADDLE);
        ItemMeta itemMeta = horsebook.getItemMeta();

        itemMeta.setDisplayName(ChatColor.RED + "Przywołanie wierzchowca klasy III");

        ArrayList<String> lore = new ArrayList<String>();
        lore.add((ChatColor.RED + "Kliknij prawym aby przywołać wierzchowca klasy 3"));
        itemMeta.setLore(lore);
        horsebook.setItemMeta(itemMeta);

        return horsebook;
    }
    private HashMap<String, List<ItemStack>> deathItems = new HashMap<>();
    @EventHandler
    public void onEntitydeathEvent(EntityDeathEvent event){
        Entity e = event.getEntity();
        if(e instanceof Horse && ((Horse) e).getOwner() != null){
            String playername =  ((Horse)e).getOwner().getName();
            Player player = Bukkit.getServer().getPlayer(playername);
            cooldown.setSystemTime(player, 1200);
            player.sendMessage(ChatColor.DARK_RED + "Twój koń zginął! Będziesz potrzebował więcej czasu aby go przywołać");
            event.getDrops().clear();
        }
        else if(e instanceof AbstractHorse && ((AbstractHorse) e).getOwner() != null){
            String playername =  ((AbstractHorse)e).getOwner().getName();
            Player player = Bukkit.getServer().getPlayer(playername);
            cooldown.setSystemTime(player, 1200);
            player.sendMessage(ChatColor.DARK_RED + "Twój koń zginął! Będziesz potrzebował więcej czasu aby go przywołać");
            event.getDrops().clear();
        }
        if(e instanceof Player){
            Player player = (Player) event.getEntity();
            List<ItemStack> items = event.getDrops();
            List<ItemStack> saveItems = new ArrayList<>();
            for(ItemStack is: items){
                if(is.isSimilar(getHorseBook1()) ||is.isSimilar(getHorseBook2()) || is.isSimilar(getHorseBook3())){
                    saveItems.add(is);
                }
            }
            event.getDrops().removeAll(saveItems);
            deathItems.put(player.getUniqueId().toString(), saveItems);
        }
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event){
        if(deathItems.containsKey(event.getPlayer().getUniqueId().toString())){
            for(ItemStack is: deathItems.get(event.getPlayer().getUniqueId().toString())){

              event.getPlayer().getInventory().addItem(is);
            }

          deathItems.remove(event.getPlayer().getUniqueId().toString());
        }
        else return;
    }


  }
