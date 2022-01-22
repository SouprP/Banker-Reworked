package me.souprpk.main.ConfigFiles;

import me.souprpk.main.Banker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class FlatFileStorageConfig {

    private Banker banker;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public void reloadConfig() {
        if(this.configFile == null)
            this.configFile = new File(banker.getMain().getDataFolder(), "storage/flatfile.yml");

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = banker.getMain().getResource("storage/flatfile.yml");
        if(defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if(this.dataConfig == null)
            reloadConfig();

        return this.dataConfig;
    }

    public void saveConfig() {
        if(this.dataConfig == null || this.configFile == null)
            return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            banker.getMain().getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
        }
    }

    public void saveDefaultConfig() {
        if(this.configFile == null)
            this.configFile = new File(banker.getMain().getDataFolder(), "storage/flatfile.yml");

        if(!this.configFile.exists())
            banker.getMain().saveResource("storage/flatfile.yml", false);

    }
}
