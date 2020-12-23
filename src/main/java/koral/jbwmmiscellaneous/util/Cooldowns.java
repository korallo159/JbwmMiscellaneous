package koral.jbwmmiscellaneous.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Cooldowns {
    private HashMap<String, Long> cooldown;

    public Cooldowns(HashMap<String, Long> cooldown) {
        this.cooldown = cooldown;
    }

    //ustawia graczu czas aktualny + INT dodatkowego czasu w sekundach
    public void setSystemTime(Player player){
        cooldown.put(player.getUniqueId().toString(), (System.currentTimeMillis() / 1000));

    }
    public void setSystemTime(Player player, Integer additionaltime){
        cooldown.put(player.getUniqueId().toString(), (System.currentTimeMillis() / 1000 + additionaltime));

    }
    //Sprawdza CZY czas gracza + dodatkowy czas Jest większy od systemowego, jeśli tak zwraca prawde i nie wykonuje, jeśli nie zwraca fałsz. // A więc przy sprawdzaniu CD trzeba uwzględnić
    // co ta funkcja zwraca.
    public boolean checkPlayerCooldown(Player player, Integer time) {
        if (cooldown.containsKey(player.getUniqueId().toString())) {
            if (cooldown.get(player.getUniqueId().toString()) + time >= (System.currentTimeMillis() / 1000)) {
                player.sendMessage(ChatColor.RED +  "Musisz odczekać jeszcze " + (cooldown.get(player.getUniqueId().toString()) + time - System.currentTimeMillis() / 1000) + "sekund");
                return true;
            } else
                return false;
        }
        else
            return false;
    }
}

