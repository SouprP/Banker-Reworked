package me.souprpk.main.StorageSystem;

import me.souprpk.main.Banker;

import java.math.BigDecimal;
import java.util.UUID;

public interface StorageSystem {

    public void deposit(UUID uuid, BigDecimal amount);
    public void withdraw(UUID uuid, BigDecimal amount);
}
