package me.souprpk.main.Systems.MoneyLoss;

import me.souprpk.main.Banker;
import org.bukkit.entity.Player;

public class MoneyLoss {

    private Banker banker = Banker.getMain();

    public void loseMoney(Player player){

        if(banker.getConfig().getBoolean("money.money-loss.enabled")){

            if(banker.getConfig().getBoolean("money.money-loss.percentage-based")){
                double percentage = banker.getConfig().getDouble("money.money-loss.percentage amount");
                double money = banker.eco.getBalance(player);

                double lostMoney = money * (percentage / 100);
                banker.eco.withdrawPlayer(player, lostMoney);
                return;
            }
            else if(banker.getConfig().getBoolean("money.money-loss.amount-based")){
                banker.eco.withdrawPlayer(player, banker.getConfig().getDouble("money.money-loss.amount-loss"));
            }
        }
    }
}
