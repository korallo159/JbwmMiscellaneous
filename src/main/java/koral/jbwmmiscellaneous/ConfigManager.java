package koral.jbwmmiscellaneous;

import com.google.common.base.Charsets;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class ConfigManager {
    public YamlConfiguration config;
    public File configFile;
    public ConfigManager(String ymlName){
        this.configFile = new File(JbwmMiscellaneous.getJbwmMiscellaneous().getDataFolder(), ymlName);
        this.config = YamlConfiguration.loadConfiguration(configFile);
        setupConfig(configFile, ymlName);
        reloadCustomConfig();
    }


    public void setupConfig(File file, String ymlName){
        if(!file.exists()){
            try{
                JbwmMiscellaneous.getJbwmMiscellaneous().saveResource(ymlName, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reloadCustomConfig(){
        config = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = JbwmMiscellaneous.getJbwmMiscellaneous().getResource(configFile.getName());
        if (defConfigStream == null) {
            return;
        }

        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

   public void saveCustomDefaultConfig(){
       if (!configFile.exists()) {
           JbwmMiscellaneous.getJbwmMiscellaneous().saveResource(configFile.getName(), false);
       }
   }
}
