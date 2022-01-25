package me.souprpk.main.Systems.MoneyHandlers;

import me.souprpk.main.Banker;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Interest {

    private Banker banker;
    int interestTime;
    BigDecimal interestRate;

    public Interest(Banker banker){
        this.banker = banker;
        this.interestTime = this.banker.getConfig().getInt("money.every-hours");
        this.interestRate = BigDecimal.valueOf(this.banker.getConfig().getDouble("money.interest-rate"));
    }

    public void checkInterestTime() {

        if(banker.flat.getConfig().getString("nextInterest") == null) {
            banker.flat.getConfig().set("nextInterest", LocalDateTime.now().plusHours(interestTime).toString());
            banker.flat.saveConfig();
            return;
        }


        if(banker.flat.getConfig().getString("nextInterest") != null)
            if(LocalDateTime.now().isAfter(LocalDateTime.parse(banker.flat.getConfig().getString("nextInterest")))) {
                if(banker.getConfig().getString("main.storage-system").equals("mysql")){
                    banker.data.interestRateIncrease(interestRate);
                }else{
                    banker.flatData.interestRateIncrease(interestRate);
                }
                banker.flat.getConfig().set("nextInterest", LocalDateTime.now().plusHours(interestTime).toString());
                banker.flat.saveConfig();
            }

    }
}
