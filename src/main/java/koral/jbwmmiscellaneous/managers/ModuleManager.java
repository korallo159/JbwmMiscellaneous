package koral.jbwmmiscellaneous.managers;

import com.google.common.collect.Lists;
import koral.jbwmmiscellaneous.JbwmMiscellaneous;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class ModuleManager {

    @Target(value= ElementType.TYPE)
    @Retention(value= RetentionPolicy.RUNTIME)
    public @interface Moduł {

    }

    private static class Module{
        boolean enabled;
        Object object;
        Class<?> clazz;
    }
    //nazwa modułu: moduł
    final static Map<String, Module> map = new HashMap<>();

    final static WyłączonyExecutor offexecutor = new WyłączonyExecutor();

    public ModuleManager() {
        for(Class<?> clazz: wszystkieKlasy()){
            if(clazz.isAnnotationPresent(Moduł.class)){
                Module module = new Module();
                module.clazz = clazz;
                map.put(clazz.getSimpleName(), module);
            }
        }
        reload();
    }

    public static List<Class<?>> wszystkieKlasy() {
        List<Class<?>> lista = Lists.newArrayList();
        try {
            JarFile jar = new JarFile("plugins/"+ JbwmMiscellaneous.getJbwmMiscellaneous().getName()+".jar");
            Enumeration<JarEntry> scieżki = jar.entries();
            while (scieżki.hasMoreElements()) {
                String sc = scieżki.nextElement().toString();
                if (!sc.endsWith(".class")) continue;
                // java/util/jar -> java.util.jar
                sc = sc.substring(0, sc.length()-6).replace('/', '.');
                try {
                    lista.add(Class.forName(sc, false, ModuleManager.class.getClassLoader()));
                } catch (Throwable e) {

                }
            }
            jar.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void reload(){
        JbwmMiscellaneous.getJbwmMiscellaneous().reloadConfig();
        FileConfiguration config = JbwmMiscellaneous.jbwmMiscellaneous.getConfig();
        for(Map.Entry<String, Module> mapa: map.entrySet()){
            //jeśli ma nie być właczone && a jest wlaczony to wylacz
            if(!config.getBoolean(mapa.getKey())&& mapa.getValue().enabled){
               unregister(mapa.getValue().object);
               mapa.getValue().enabled = false;
            }
            //jeśli ma być włączone && a jest wyłączony to włącz
            else if(config.getBoolean(mapa.getKey())&& !mapa.getValue().enabled){
                if(mapa.getValue().object == null){
                    try {
                        mapa.getValue().object = mapa.getValue().clazz.newInstance();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                if(mapa.getValue().object != null){
                    register(mapa.getValue().object);
                    mapa.getValue().enabled = true;
                }
            }
        }
    }

    private static void register(Object object){
        if(object instanceof Listener){
            Bukkit.getServer().getPluginManager().registerEvents((Listener) object, JbwmMiscellaneous.getJbwmMiscellaneous());
        }
        if(object instanceof CommandManager){
            CommandManager commandManager = (CommandManager) object;
            for(PluginCommand command: commandManager._komendy){
                command.setExecutor(commandManager);
                command.setTabCompleter(commandManager);
            }
        }
    }
    private static void unregister(Object object){
        if(object instanceof Listener){
            HandlerList.unregisterAll((Listener) object);
        }
        if(object instanceof CommandManager){
            CommandManager commandManager = (CommandManager) object;
            for(PluginCommand command: commandManager._komendy){
                command.setExecutor(offexecutor);
                command.setTabCompleter(offexecutor);
            }
        }
    }

    static class WyłączonyExecutor implements TabExecutor {
        @Override
        public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
            arg0.sendMessage("§cTa komenda jest aktualnie wyłączona");
            return true;
        }
        @Override
        public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
            return null;
        }
    }

}
