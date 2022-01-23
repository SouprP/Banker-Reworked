package me.souprpk.main.Systems.MoneyHandlers;

import me.souprpk.main.Banker;

import java.time.LocalDateTime;

public class Interest {

    private Banker banker;
    int interestTime;
    double interestRate;

    public Interest(Banker banker){
        this.banker = banker;
        this.interestTime = this.banker.getConfig().getInt("every-hours");
        this.interestRate = this.banker.getConfig().getDouble("interest-rate");
    }

    public void checkInterestTime() {

       /* if(banker.playerData.getConfig().getString("nextInterest") == null) {
            banker.playerData.getConfig().set("nextInterest", LocalDateTime.now().plusHours(interestTime).toString());
            banker.playerData.saveConfig();
            return;
        }


        if(banker.playerData.getConfig().getString("nextInterest") != null)
            if(LocalDateTime.now().isAfter(LocalDateTime.parse(banker.playerData.getConfig().getString("nextInterest")))) {
                banker.interestRateIncrease(interestRate);
                banker.playerData.getConfig().set("nextInterest", LocalDateTime.now().plusHours(interestTime).toString());
                banker.playerData.saveConfig();
            }*/

    }
}
