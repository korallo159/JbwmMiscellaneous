package koral.jbwmmiscellaneous.modules;

import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;
@ModuleManager.Moduł
public class QuestAddons extends CommandManager implements Listener {

    public QuestAddons() {
        super("npcmsg", "/npcmsg <nick> tresc");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) return false;
        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Taki gracz nie jest online!");
            return true;
        }
        else{
            if(args.length >= 2){
                StringBuilder builder = new StringBuilder();
                for(int i = 1; i < args.length; i++){
                    builder.append(args[i]).append(" ");;
                }
                target.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', builder.toString())));
            }
        }
        return true;
    }
}
