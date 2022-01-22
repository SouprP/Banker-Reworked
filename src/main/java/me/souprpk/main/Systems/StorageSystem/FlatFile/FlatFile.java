package me.souprpk.main.Systems.StorageSystem.FlatFile;

import me.souprpk.main.Banker;
import me.souprpk.main.Systems.StorageSystem.StorageSystem;

import java.math.BigDecimal;
import java.util.UUID;

public class FlatFile implements StorageSystem {

    private Banker banker;

    public FlatFile(Banker banker){
        this.banker = banker;
    }

    @Override
    public void deposit(UUID uuid, BigDecimal amount) {

    }

    @Override
    public void withdraw(UUID uuid, BigDecimal amount) {

    }

    @Override
    public BigDecimal getMoney(UUID uuid) {
        return BigDecimal.valueOf(banker.flat.getConfig().getDouble("players." + uuid.toString() + ".bank-money"));
    }
}
