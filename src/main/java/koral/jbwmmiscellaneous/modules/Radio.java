package koral.jbwmmiscellaneous.modules;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import koral.jbwmmiscellaneous.JbwmMiscellaneous;
import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import koral.jbwmmiscellaneous.util.Cooldowns;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
@ModuleManager.Moduł
public class Radio extends CommandManager implements Listener {
    Cooldowns cooldown = new Cooldowns(new HashMap<>());


    public Radio() {
        super("radio", "/radio <wiadomosc>");
        JbwmMiscellaneous.addPerm("JbwmMiscellaneous.radio");
        JbwmMiscellaneous.addPerm("JbwmMiscellaneous.radio.admin");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if(args.length == 0) return false;
        if(args[0].equalsIgnoreCase("daj") && player.hasPermission("JbwmMiscellaneous.radio.admin")) {
            player.getInventory().addItem(itemStack());
            return true;
        }
        if(args.length > 0) {
            boolean playerHave = false;
            for(ItemStack is: player.getInventory().getContents()) {
                if (is != null && is.getItemMeta().getDisplayName().equals("§4§lPrzekaznik radiowy")) {
                    playerHave = true;
                    if (!cooldown.checkPlayerCooldown(player, 360)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < args.length; i++)
                            stringBuilder.append(args[i] + " ");

                        String s = stringBuilder.toString();
                        Bukkit.broadcastMessage("§2[§aPrzekaźnik radiowy§2]§7 " + s);
                        cooldown.setSystemTime(player);
                        return true;
                    }
                }
            }
            if(!playerHave)
                player.sendMessage("§4Nie posiadasz przy sobie przekaźnika radiowego!");
        }
        return true;
    }



    private ItemStack itemStack(){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ4YThjNTU4OTFkZWM3Njc2NDQ0OWY1N2JhNjc3YmUzZWU4OGEwNjkyMWNhOTNiNmNjN2M5NjExYTdhZiJ9fX0="));
        try{
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        meta.setDisplayName("§4§lPrzekaznik radiowy");
        List<String> lore = new ArrayList<>();
        lore.add("§7Posiadając ten przedmiot w ekwipunku");
        lore.add("§7co jakiś czas jesteś w stanie złapać odpowiednią falę");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }


}
