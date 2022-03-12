package me.souprpk.main.Events;

import me.souprpk.main.Systems.MoneyLoss.MoneyLoss;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {

    public void onDeath(PlayerDeathEvent event){
        MoneyLoss moneyLoss = new MoneyLoss();
        moneyLoss.loseMoney(event.getEntity());
        moneyLoss = null;
    }
}
