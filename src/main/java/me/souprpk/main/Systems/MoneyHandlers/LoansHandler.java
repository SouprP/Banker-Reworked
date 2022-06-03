package me.souprpk.main.Systems.MoneyHandlers;

import me.souprpk.main.Banker;
import me.souprpk.main.Tools.Transaction;
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
        if(banker.flat.getConfig().getDouble("players." + uuid.toString() + ".loan") > 0)
            Bukkit.getPlayer(uuid).sendMessage(new Utilities(banker).Translate(banker.messageConfig.getConfig().getString("loan-error")));
        banker.flat.getConfig().set("players." + uuid.toString() + ".loan", increaseDebt(amount));
        //banker.flat.getConfig().set("players." + uuid.toString() + ".loan-taken", true);
        banker.flat.saveConfig();
        new Utilities(banker).playSound(Bukkit.getPlayer(uuid), Transaction.TransactionType.LoanTake);
    }

    public void payUpDebt(UUID uuid, BigDecimal amount){
        // amount = 1, 2, 10
        BigDecimal debt = BigDecimal.valueOf(banker.flat.getConfig().getDouble("players." + uuid.toString() + ".loan"));
        BigDecimal moneyOnPlayer = BigDecimal.valueOf(banker.eco.getBalance(Bukkit.getPlayer(uuid))).divide(amount);

        if(moneyOnPlayer.compareTo(debt) <= 0){
            banker.eco.withdrawPlayer(Bukkit.getPlayer(uuid), moneyOnPlayer.doubleValue());
            banker.flat.getConfig().set("players." + uuid.toString() + ".loan", debt.subtract(moneyOnPlayer));
        }else{
            banker.eco.withdrawPlayer(Bukkit.getPlayer(uuid), debt.doubleValue());
            banker.flat.getConfig().set("players." + uuid.toString() + ".loan", 0);
        }
        banker.flat.saveConfig();
        new Utilities(banker).playSound(Bukkit.getPlayer(uuid), Transaction.TransactionType.DebtPay);
    }

    public BigDecimal increaseDebt(BigDecimal amount){
        return amount.add(amount.multiply(BigDecimal.valueOf(banker.getConfig().getDouble("money.loans.loan-interest-rate") / 100)));
    }

    public BigDecimal getDebt(UUID uuid){
        return BigDecimal.valueOf(banker.flat.getConfig().getDouble("players." + uuid.toString() + ".loan"));
    }
}
