package me.souprpk.main;

import me.souprpk.main.Commands.*;
import me.souprpk.main.ConfigFiles.FlatFileStorageConfig;
import me.souprpk.main.ConfigFiles.MessageConfig;
import me.souprpk.main.Events.InventoryEvent;
import me.souprpk.main.Systems.Discord.DiscordHandle;
import me.souprpk.main.Systems.Logging.Logging;
import me.souprpk.main.Systems.MoneyHandlers.Interest;
import me.souprpk.main.Systems.StorageSystem.FlatFile.FlatFile;
import me.souprpk.main.Systems.StorageSystem.MySQL.MySQL;
import me.souprpk.main.Systems.StorageSystem.MySQL.SQLGetter;
import me.souprpk.main.Tools.UpdateChecker;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Banker extends JavaPlugin {

    // Main parts of a plugin
    public static Banker main;
    public Economy eco;

    // Other essential parts of a plugin
    public MessageConfig messageConfig;
    public MySQL mySQL;
    public SQLGetter data;
    public FlatFileStorageConfig flat;
    public FlatFile flatData;
    public Logging logging;
    public Interest interest;
    public DiscordHandle discordHandle;
    private final int pluginId = 11913;

    @Override
    public void onEnable() {
        main = this;
        if(!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "You must have Vault and an Economy plugin installed, disabling plugin");
            getServer().getPluginManager().disablePlugin(this);

            return;
        }

        // Enable modules of a plugin
        messageConfig = new MessageConfig(main);
        flat = new FlatFileStorageConfig(main);
        interest = new Interest(main);
        flatData = new FlatFile(main);
        logging = new Logging(main);
        if(this.getConfig().getString("main.storage-system").equals("mysql")){
            mySQL = new MySQL(main);
            data = new SQLGetter(main);

            try {
                mySQL.connect();
            } catch (ClassNotFoundException | SQLException e) {
                //e.printStackTrace();
            }

            // Checks if MySQL is connected to a database
            if(mySQL.isConnected()) {
                Bukkit.getLogger().info("[Banker] Database is connected!");
                data.createTable();
                flatData.writeToMySQL();
            }
        }

        if(this.getConfig().getBoolean("discord.enabled"))
            discordHandle = new DiscordHandle();

        this.saveDefaultConfig();
        messageConfig.saveDefaultConfig();
        flat.saveDefaultConfig();

        this.getServer().getPluginManager().registerEvents(new InventoryEvent(), this);
        this.getCommand("banker").setExecutor(new BankerCommand());
        this.getCommand("banker").setTabCompleter(new BankerCommandTab());
        this.getCommand("bank").setExecutor(new BankCommand());
        this.getCommand("bank").setTabCompleter(new BankCommandTab());;

        // Check interest time and increase players money
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                interest.checkInterestTime();
            }
        }, 0, 200);

        Metrics metrics = new Metrics(this, pluginId);
        logging.log("Banker plugin enabled successfully!");

        new UpdateChecker(this, 99521).getVersion(version -> {
            //Bukkit.getConsoleSender().sendMessage(Color.GREEN + "Banker version: " + version);
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("There is no new update available.");
            } else {
                getLogger().info("There is a new update available!");
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(this.getConfig().getString("main.storage-system").equals("mysql"))
            data.writeToFlatFile();
        if(mySQL != null)
            if(mySQL.isConnected())
                mySQL.disconnect();

        if(this.discordHandle.getJda() != null) this.discordHandle.getJda().shutdown();

        logging.log("Banker plugin disabled!");
    }

    public static Banker getMain(){
        return main;
    }

    // Setup Economy
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economy = getServer().getServicesManager()
                .getRegistration(net.milkbowl.vault.economy.Economy.class);

        if(economy != null)
            eco = economy.getProvider();
        return(eco != null);
    }
}
