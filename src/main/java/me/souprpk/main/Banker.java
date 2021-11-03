package me.souprpk.main;

import org.bukkit.plugin.java.JavaPlugin;

public final class Banker extends JavaPlugin {

    private Banker main;
    @Override
    public void onEnable() {
        main = this;
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Banker getMain(){
        return main;
    }
}
