package me.souprpk.main.Systems.StorageSystem.FlatFile;

import me.souprpk.main.Banker;
import me.souprpk.main.Systems.StorageSystem.StorageSystem;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.UUID;

public class FlatFile implements StorageSystem {

    private Banker banker;

    public FlatFile(Banker banker){
        this.banker = banker;
    }

    @Override
    public void deposit(UUID uuid, BigDecimal amount) {
        BigDecimal money = BigDecimal.valueOf(banker.flat.getConfig().getDouble("players." + uuid.toString() + ".deposited-money"));

        banker.flat.getConfig().set("players." + uuid.toString() + ".deposited-money", money.add(amount));
        banker.flat.saveConfig();
    }

    @Override
    public void withdraw(UUID uuid, BigDecimal amount) {
        BigDecimal money = BigDecimal.valueOf(banker.flat.getConfig().getDouble("players." + uuid.toString() + ".deposited-money"));
        banker.flat.getConfig().set("players." + uuid.toString() + ".deposited-money", money.subtract(amount));
        banker.flat.saveConfig();
    }

    public void createPlayer(UUID uuid){
        if(!exists(uuid)){
            banker.flat.getConfig().set("players." + uuid.toString() + ".deposited-money", 0.0);
            banker.flat.saveConfig();
        }
    }

    private boolean exists(UUID uuid){
        if(banker.flat.getConfig().getString("players." + uuid.toString()) != null)
            return true;
        return false;
    }

    @Override
    public BigDecimal getMoney(UUID uuid) {
        return BigDecimal.valueOf(banker.flat.getConfig().getDouble("players." + uuid.toString() + ".deposited-money"));
    }

    public void writeCustom(String uuid, BigDecimal amount){
        banker.flat.getConfig().set("players." + uuid.toString() + ".deposited-money", amount);
        banker.flat.saveConfig();
    }

    public void writeToMySQL(){
        banker.flat.getConfig().getConfigurationSection("players").getKeys(false).forEach(uuid -> {
            banker.data.writeCustom(uuid, BigDecimal.valueOf(banker.flat.getConfig().getDouble("players." + uuid + ".deposited-money")));
        });
    }
}
