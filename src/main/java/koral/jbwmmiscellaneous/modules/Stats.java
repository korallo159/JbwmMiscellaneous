package koral.jbwmmiscellaneous.modules;

import koral.jbwmmiscellaneous.managers.CommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.util.List;

public class Stats extends CommandManager implements Listener {

    public Stats() {
        super("stats");
    }



    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return false;
    }
}
