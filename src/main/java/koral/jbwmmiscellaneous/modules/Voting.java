package koral.jbwmmiscellaneous.modules;
import koral.jbwmmiscellaneous.JbwmMiscellaneous;
import koral.jbwmmiscellaneous.managers.CommandManager;
import koral.jbwmmiscellaneous.managers.ModuleManager;
import koral.jbwmmiscellaneous.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@ModuleManager.Moduł
public class Voting extends CommandManager implements Listener, TabCompleter {
    private int votingTime;
    private BukkitTask bukkitTask = null;
    Map<String, Boolean> day = new HashMap<>();
    Map<String, Boolean> night = new HashMap<>();
    Map<String, Boolean> force = new HashMap<>();
    private ConfigManager voting = new ConfigManager("voting.yml");
    public Voting() {
        super("vote", "/vote force/day/night/reload", "glosowanie");
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length == 0)
                return false;
            switch (args[0]) {
                case "day":
                    if(bukkitTask == null || bukkitTask.isCancelled() || day.containsKey(player.getUniqueId().toString())) {
                        player.sendMessage(ChatColor.RED + "Aby zagłosować musi być aktywne głosowanie!");
                    }
                    else {
                        player.sendMessage("Zagłosowano na dzień");
                        day.put(player.getUniqueId().toString(), true);
                    }
                    break;
                case "night":
                    if(bukkitTask == null || bukkitTask.isCancelled() || night.containsKey(player.getUniqueId().toString())) {
                        player.sendMessage(ChatColor.RED + "Aby zagłosować musi być aktywne głosowanie!");
                    }
                    else {
                        player.sendMessage("Zagłosowano na dzień");
                        night.put(player.getUniqueId().toString(), true);
                    }
                    break;
                case "force":
                    if(player.hasPermission("vote.admin")) {
                        if (bukkitTask == null) {
                            runVotingTimer();
                        } else {
                            if (!bukkitTask.isCancelled()) {
                                player.sendMessage("Może być tylko jedno aktywne głosowanie!");
                                return false;
                            }

                            runVotingTimer();
                        }
                    }
                    else{
                        if(force.containsKey(player.getUniqueId().toString())) return false;
                        force.put(player.getUniqueId().toString(), true);
                       int required = Bukkit.getOnlinePlayers().size() / 3 - force.size();
                        if(force.size() > Bukkit.getOnlinePlayers().size()/ 3){
                            Bukkit.broadcastMessage(ChatColor.RED + "Ponad 33% graczy wyraziło chęć zmiany pory dnia, startowanie głosowania...");
                            force.clear();
                            if(bukkitTask == null || bukkitTask.isCancelled())
                            runVotingTimer();
                        }
                        else
                            player.sendMessage(ChatColor.DARK_RED + "Potrzebne są jeszcze " + required + " głosy, aby wymusić głosowanie na zmiane dnia");
                    }
                    break;
                case "reload":
                    if(player.hasPermission("vote.admin")){
                        voting.reloadCustomConfig();
                    }
            }
        }
        return true;
    }
    private void checkVotesAndDoThing(){
        if(day.size() > night.size()){
            for(World world:Bukkit.getWorlds())
                world.setTime(0);

        }
        else if(day.size() == night.size()) {
            day.clear();
            night.clear();
            return;
        }
        else {
            for(World world:Bukkit.getWorlds())
                world.setTime(10000);

        }
        day.clear();
        night.clear();
    }
    private void runVotingTimer() {
        votingTime = voting.config.getInt("votingTime");
            bukkitTask = new BukkitRunnable() {
                @Override
                public void run() {
                    int timeleft = votingTime--;
                        Bukkit.getOnlinePlayers().forEach(player1 -> player1.sendActionBar((ChatColor.RED + "Liczby głosów: Dzień: " + day.size() + " Noc: " + night.size() + " " +ChatColor.YELLOW + timeleft)));
                    if(votingTime == 0){
                        checkVotesAndDoThing();
                        cancel();

                    }
                }
            }.runTaskTimer(JbwmMiscellaneous.getJbwmMiscellaneous(), 0, 20);
    }

    public void autoVoteEvent(){
        long autoVoteTime = 50;
        new BukkitRunnable() {
            @Override
            public void run() {
                    runVotingTimer();
            }
        }.runTaskTimer(JbwmMiscellaneous.getJbwmMiscellaneous(), 0, autoVoteTime * 20);
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        completions.add("day");
        completions.add("night");
        completions.add("force");
        completions.add("reload");
        if(command.getName().equals("vote")){
            return completions;
        }
        return null;
    }
}
