package me.souprpk.main.StorageSystem.File;

import me.souprpk.main.Banker;
import me.souprpk.main.StorageSystem.StorageSystem;

import java.math.BigDecimal;
import java.util.UUID;

public class FlatFile implements StorageSystem {

    private Banker banker;

    @Override
    public void deposit(UUID uuid, BigDecimal amount) {

    }

    @Override
    public void withdraw(UUID uuid, BigDecimal amount) {

    }
}
