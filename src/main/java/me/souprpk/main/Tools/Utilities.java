package me.souprpk.main.Tools;

import me.souprpk.main.Banker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utilities {

    private final Banker banker;

    public Utilities(Banker banker){
        this.banker = banker;
    }

    public String Translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public BigDecimal getMoney(Player player){
        return BigDecimal.valueOf(banker.eco.getBalance(player));
    }

    public String getConfigString(String mess){
        return banker.getConfig().getString(mess);
    }

    public boolean getConfigBoolean(String mess){
        return banker.getConfig().getBoolean(mess);
    }

    public BigDecimal getConfigDouble(String mess){
        return BigDecimal.valueOf(banker.getConfig().getDouble(mess));
    }

    public BigDecimal truncateDecimal(final BigDecimal x, final int numberOfDecimals) {
        return new BigDecimal(String.valueOf(x)).setScale(numberOfDecimals, RoundingMode.DOWN); // BigDecimal.ROUND_DOWN
    }

    public String Translate(String string, int n) {
        Bukkit.getConsoleSender().sendMessage("THIS IS YOUR STRING: " + ChatColor.translateAlternateColorCodes('&', string));
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}