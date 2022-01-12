package me.souprpk.main;

import me.souprpk.main.ConfigFiles.MessageConfig;
import me.souprpk.main.Discord.DiscordHandle;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Banker extends JavaPlugin {

    // Main parts of a plugin
    private Banker main;
    public Economy eco;

    // Other essential parts of a plugin
    public MessageConfig messageConfig;
    public DiscordHandle discordHandle;

    @Override
    public void onEnable() {
        main = this;
        if(!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "You must have Vault and an Economy plugin installed, disabling plugin");
            getServer().getPluginManager().disablePlugin(this);

            return;
        }

        // Enable modules of a plugin
        messageConfig = new MessageConfig();
        discordHandle = new DiscordHandle();

        messageConfig.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Banker getMain(){
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
