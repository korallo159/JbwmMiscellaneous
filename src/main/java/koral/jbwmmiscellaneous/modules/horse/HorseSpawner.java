package koral.jbwmmiscellaneous.modules.horse;

import com.google.common.base.Preconditions;
import koral.jbwmmiscellaneous.JbwmMiscellaneous;
import koral.jbwmmiscellaneous.managers.ConfigManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class HorseSpawner {
    private ConfigManager plugin = new ConfigManager("OwnHorse.yml");

    private Location FirstLoc(Player player, double radius){

        return new Location(player.getWorld(),
                player.getLocation().getX() - radius,
                getYBelowLoc(player) + 1,
                player.getLocation().getZ() - radius);
    }

    private Location SecondLoc(Player player, double radius){
        return new Location(player.getWorld(),
                player.getLocation().getX() + radius,
                getYBelowLoc(player) + 1,
                player.getLocation().getZ() + radius);
    }

    private Location getRandomLocation(Location loc1, Location loc2) {
        Preconditions.checkArgument(loc1.getWorld() == loc2.getWorld());
        double minX = Math.min(loc1.getX(), loc2.getX());
        double minY = Math.min(loc1.getY(), loc2.getY());
        double minZ = Math.min(loc1.getZ(), loc2.getZ());

        double maxX = Math.max(loc1.getX(), loc2.getX());
        double maxY = Math.max(loc1.getY(), loc2.getY());
        double maxZ = Math.max(loc1.getZ(), loc2.getZ());

        return new Location(loc1.getWorld(), randomDouble(minX, maxX), randomDouble(minY, maxY), randomDouble(minZ, maxZ));
    }

    private double randomDouble(double min, double max) {
        return min + ThreadLocalRandom.current().nextDouble(Math.abs(max - min + 1));
    }


    private double getYBelowLoc(Player player) {
        int topX = player.getLocation().getBlockX();
        int topZ = player.getLocation().getBlockZ();
        return player.getLocation().getWorld().getHighestBlockYAt(topX, topZ);
    }

    private void horseAttributes(Horse horse, double hp, double speed, double strength){
        Attributable horseAttributable = horse;
        AttributeInstance ai1 = horseAttributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        AttributeInstance ai2 = horseAttributable.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        ai1.setBaseValue(hp);
        ai2.setBaseValue(speed);
        horse.setHealth(hp);
        horse.setJumpStrength(strength);
    }

    private void abstractHorseAttributes(AbstractHorse horse, double hp, double speed, double strength){
        Attributable horseAttributable = horse;
        AttributeInstance ai1 = horseAttributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        AttributeInstance ai2 = horseAttributable.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        ai1.setBaseValue(hp);
        ai2.setBaseValue(speed);
        horse.setHealth(hp);
        horse.setJumpStrength(strength);
    }

    public void SummonDefaultPlayerHorse(Player player) {
        Location randomloc = getRandomLocation(FirstLoc(player, 10), SecondLoc(player, 10));
        for (int i = 0; i < 10; i++) {
            if (randomloc.getBlock().getType().isEmpty() || randomloc.getBlock().getType().equals(Material.GRASS) || randomloc.getBlock().getType().equals(Material.TALL_GRASS)) {
                Horse horse = (Horse) player.getWorld().spawnEntity(randomloc, EntityType.HORSE);
                horse.setCustomName(plugin.getConfig().getString("horseof") + player.getName());
                horse.setBreed(false);

                horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                horse.setColor(Horse.Color.WHITE);
                horse.setRemoveWhenFarAway(true);
                horseAttributes(horse, plugin.getConfig().getDouble("defaulthorsehp"),plugin.getConfig().getDouble("defaulthorsespeed"),plugin.getConfig().getDouble("defaulthorsestrength"));
                horse.setOwner(player);
                player.sendMessage(ChatColor.GREEN + "Przywołałeś wierzchowca, powinien być gdzieś w pobliżu");
                break;
            }
            if (i == 9) {
                player.sendMessage(ChatColor.RED + "Nie można przywołać konia w tym miejscu!");
            }
        }
    }

    public void SummonBattlePlayerHorse(Player player) {
        Location randomloc = getRandomLocation(FirstLoc(player, 10), SecondLoc(player, 10));
        for (int i = 0; i < 10; i++) {
            if (randomloc.getBlock().getType().isEmpty() || randomloc.getBlock().getType().equals(Material.GRASS) || randomloc.getBlock().getType().equals(Material.TALL_GRASS)) {
                Horse horse = (Horse) player.getWorld().spawnEntity(randomloc, EntityType.HORSE);
                horse.setCustomName(plugin.getConfig().getString("horseof") + player.getName());
                horse.setBreed(false);
                horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                horse.getInventory().setArmor(new ItemStack(Material.IRON_HORSE_ARMOR));
                horse.setColor(Horse.Color.DARK_BROWN);
                horseAttributes(horse, plugin.getConfig().getDouble("battlehorsehp"),plugin.getConfig().getDouble("battlehorsespeed"),plugin.getConfig().getDouble("battlehorsestrength"));
                horse.setOwner(player);
                player.sendMessage(ChatColor.GREEN + "Przywołałeś wierzchowca, powinien być gdzieś w pobliżu");
                break;
            }
            if (i == 9)
                player.sendMessage(ChatColor.RED + "Nie można przywołać konia w tym miejscu!");
        }
    }

    public void SummonMilitaryPlayerHorse(Player player) {
        Location randomloc = getRandomLocation(FirstLoc(player, 10), SecondLoc(player, 10));
        for (int i = 0; i < 10; i++) {
            if (randomloc.getBlock().getType().isEmpty() || randomloc.getBlock().getType().equals(Material.GRASS) || randomloc.getBlock().getType().equals(Material.TALL_GRASS)) {
                Horse horse = (Horse) player.getWorld().spawnEntity(randomloc, EntityType.HORSE);
                horse.setCustomName(plugin.getConfig().getString("horseof") + player.getName());
                horse.setBreed(false);
                horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                horse.getInventory().setArmor(new ItemStack(Material.IRON_HORSE_ARMOR));
                horse.setColor(Horse.Color.BLACK);
                horseAttributes(horse, plugin.getConfig().getDouble("militaryhorsehp"),plugin.getConfig().getDouble("militaryhorsespeed"),plugin.getConfig().getDouble("militaryhorsestrength"));
                horse.setRemoveWhenFarAway(true);
                horse.setOwner(player);
                player.sendMessage(ChatColor.GREEN + "Przywołałeś wierzchowca, powinien być gdzieś w pobliżu");
                break;
            }
            if (i == 9)
                player.sendMessage(ChatColor.RED + "Nie można przywołać konia w tym miejscu!");

        }
    }

    public void SummonSkeletonPlayerHorse(Player player) {
        Location randomloc = getRandomLocation(FirstLoc(player, 10), SecondLoc(player, 10));
        for (int i = 0; i < 10; i++) {
            if (randomloc.getBlock().getType().isEmpty() || randomloc.getBlock().getType().equals(Material.GRASS) || randomloc.getBlock().getType().equals(Material.TALL_GRASS)) {
                AbstractHorse horse = (AbstractHorse) player.getWorld().spawnEntity(randomloc, EntityType.SKELETON_HORSE);
                horse.setCustomName(plugin.getConfig().getString("horseof") + player.getName());
                horse.setBreed(false);
                horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                horse.setRemoveWhenFarAway(true);
                abstractHorseAttributes(horse, plugin.getConfig().getDouble("skeletonhorsehp"),plugin.getConfig().getDouble("skeletonhorsespeed"),plugin.getConfig().getDouble("skeletonhorsestrength"));
                horse.setOwner(player);
                player.sendMessage(ChatColor.GREEN + "Przywołałeś wierzchowca, powinien być gdzieś w pobliżu");
                break;
            }
            if (i == 9)
                player.sendMessage(ChatColor.RED + "Nie można przywołać konia w tym miejscu!");

        }
    }

    public void SummonZombiePlayerHorse(Player player) {
        Location randomloc = getRandomLocation(FirstLoc(player, 10), SecondLoc(player, 10));
        for (int i = 0; i < 10; i++) {
            if (randomloc.getBlock().getType().isEmpty() || randomloc.getBlock().getType().equals(Material.GRASS) || randomloc.getBlock().getType().equals(Material.TALL_GRASS)){
                AbstractHorse horse = (AbstractHorse) player.getWorld().spawnEntity(randomloc, EntityType.ZOMBIE_HORSE);
                horse.setCustomName(plugin.getConfig().getString("horseof") + player.getName());
                horse.setBreed(false);
                horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                horse.setRemoveWhenFarAway(true);
                abstractHorseAttributes(horse, plugin.getConfig().getDouble("zombiehorsehp"),plugin.getConfig().getDouble("zombiehorsespeed"),plugin.getConfig().getDouble("zombiehorsestrength"));
                horse.setOwner(player);
                JbwmMiscellaneous.log(plugin.getConfig().getDouble("zombiehorsehp"));
                player.sendMessage(ChatColor.GREEN + "Przywołałeś wierzchowca, powinien być gdzieś w pobliżu");
                break;
            }
            if (i == 9)
                player.sendMessage(ChatColor.RED + "Nie można przywołać konia w tym miejscu!");

        }
    }



    public void checkHorsePlayer(Player player){
        for(World w : Bukkit.getWorlds()){
            for(Entity e : w.getEntities()){
                if(e instanceof Horse)
                    if(((Horse) e).getOwner() != null)
                        if(((Horse) e).getOwner().getName().equals(player.getName()))
                            e.remove();

                if(e instanceof AbstractHorse)
                    if(((AbstractHorse) e).getOwner() != null)
                        if(((AbstractHorse) e).getOwner().getName().equals(player.getName()))
                            e.remove();
            }
        }

    }

}
