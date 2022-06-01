package me.souprpk.main.Systems.MoneyHandlers;

import me.souprpk.main.Banker;
import me.souprpk.main.Tools.Utilities;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.UUID;

public class LoansHandler {

    private final Banker banker;

    public LoansHandler(Banker banker){
        this.banker = banker;
    }

    public void takeLoan(UUID uuid, BigDecimal amount){
        if(banker.flat.getConfig().getBoolean("players." + uuid.toString() + ".loan-taken"))
            Bukkit.getPlayer(uuid).sendMessage(new Utilities(banker).Translate(banker.messageConfig.getConfig().getString("loan-error")));
        banker.flat.getConfig().set("players." + uuid.toString() + ".loan", increaseDebt(amount));
        banker.flat.getConfig().set("players." + uuid.toString() + ".loan-taken", true);
        banker.flat.saveConfig();
    }

    public void payUpLoan(UUID uuid, BigDecimal amount){
        // amount = 1, 2, 10
        BigDecimal money = BigDecimal.valueOf(banker.flat.getConfig().getDouble("players." + uuid.toString() + ".loan"));
        banker.flat.getConfig().set("players." + uuid.toString() + ".deposited-money", money.subtract(amount));
        banker.flat.saveConfig();
    }

    public BigDecimal increaseDebt(BigDecimal amount){
        return amount.add(amount.multiply(BigDecimal.valueOf(banker.getConfig().getDouble("money.loan-interest-rate") / 100)));
    }

    public BigDecimal getDebt(UUID uuid){
        return BigDecimal.valueOf(banker.flat.getConfig().getDouble("players." + uuid.toString() + ".loan"));
    }
}
