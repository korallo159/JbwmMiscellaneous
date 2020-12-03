package koral.jbwmmiscellaneous;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
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

public class Manager {

    @Target(value= ElementType.TYPE)
    @Retention(value= RetentionPolicy.RUNTIME)
    public @interface Moduł {

    }

    private static class Module{
        boolean enabled;
        Object object;
        Class<?> clazz;
    }


    public Manager() {
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
            JarFile jar = new JarFile("plugins/"+JbwmMiscellaneous.getJbwmMiscellaneous().getName()+".jar");
            Enumeration<JarEntry> scieżki = jar.entries();
            while (scieżki.hasMoreElements()) {
                String sc = scieżki.nextElement().toString();
                if (!sc.endsWith(".class")) continue;
                // java/util/jar -> java.util.jar
                sc = sc.substring(0, sc.length()-6).replace('/', '.');
                try {
                    lista.add(Class.forName(sc, false, Manager.class.getClassLoader()));
                } catch (Throwable e) {

                }
            }
            jar.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private void reload(){
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
                        register(mapa.getValue().object);
                        mapa.getValue().enabled = true;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    //nazwa modułu: moduł
    final Map<String, Module> map = new HashMap<>();

    private void register(Object object){
        if(object instanceof Listener){
            Bukkit.getServer().getPluginManager().registerEvents((Listener) object, JbwmMiscellaneous.getJbwmMiscellaneous());
        }
    }
    private void unregister(Object object){
        if(object instanceof Listener){
            HandlerList.unregisterAll((Listener) object);
        }
    }
}
