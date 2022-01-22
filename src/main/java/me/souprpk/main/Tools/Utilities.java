package me.souprpk.main.Tools;

import me.souprpk.main.Banker;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class Utilities {

    private Banker banker;
    public String Translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public BigDecimal getMoney(Player player){
        return BigDecimal.valueOf(banker.eco.getBalance(player));
    }

    public String getConfigString(String mess){
        return banker.getMain().getConfig().getString(mess);
    }

    public boolean getConfigBoolean(String mess){
        return banker.getMain().getConfig().getBoolean(mess);
    }

    public BigDecimal getConfigDouble(String mess){
        return BigDecimal.valueOf(banker.getMain().getConfig().getDouble(mess));
    }

    public BigDecimal truncateDecimal(final BigDecimal x, final int numberofDecimals) {
        return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_DOWN);
    }
}