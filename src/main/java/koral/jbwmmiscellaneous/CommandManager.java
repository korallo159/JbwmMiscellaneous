package koral.jbwmmiscellaneous;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;

import java.lang.reflect.Field;

public abstract class CommandManager implements CommandExecutor {

    public CommandManager(String commandName) {
        JbwmMiscellaneous.getJbwmMiscellaneous().getCommand(commandName).setExecutor(this);

    }


    public void mapCommand(PluginCommand command) {
        try{
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(JbwmMiscellaneous.getJbwmMiscellaneous().getName(), command);
    /*for(String alias : aliases) //For registering aliases
        commandMap.register(alias, "prefixname", this);*/
        } catch (NoSuchFieldException  | IllegalArgumentException | IllegalAccessException exception){
            exception.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
