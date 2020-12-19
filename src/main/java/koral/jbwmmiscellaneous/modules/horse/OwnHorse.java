package koral.jbwmmiscellaneous.modules.horse;

import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ConfigManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.util.List;

@ModuleManager.Moduł
public class OwnHorse extends CommandManager implements Listener {
    private ConfigManager horseConfig = new ConfigManager("OwnHorse");
    public OwnHorse() {
        super("horse");
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
