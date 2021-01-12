package koral.jbwmmiscellaneous.modules;

import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ConfigManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ModuleManager.Moduł
public class Birthday extends CommandManager implements Listener {


    ConfigManager birthdayConfig = new ConfigManager("birthday.yml");

    public Birthday() {
        super("urodziny");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        try {
            compareDates(player);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void compareDates(Player player) throws ParseException {
        Date tempDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        List<String> urodziny = new ArrayList<>();
        for(String s: birthdayConfig.config.getKeys(false)){
            String data = birthdayConfig.getConfig().getString(s + ".data");
            urodziny.add(data);
        }

        //TODO cos zle parsuje i wychodzi zly before
        for(int i = 0; i<urodziny.size() - 1; i++){
            String data1 = urodziny.get(i);
            String data2 = urodziny.get(i+1);
            Date finalDate1 = sdf.parse(data1);
            Date finalDate2 = sdf.parse(data2);
                if(finalDate1.before(finalDate2)){
                   tempDate =  finalDate1;
                }
        }
        String birthday = sdf.format(tempDate);
        System.out.println(birthday);
        for(String s: birthdayConfig.getConfig().getKeys(false)){
            if(birthdayConfig.getConfig().getString(s + ".data").equals(birthday)){
                int year = Year.now().getValue();
                int hisher = birthdayConfig.getConfig().getInt(s + ".rok");
                player.sendMessage("§e§lNajszybciej urodziny ma " + s + ", data urodzin to " + birthday + "§e§l będą to już jego/jej " +
                        (year - hisher) + " urodziny");
            }
        }

    }

}
