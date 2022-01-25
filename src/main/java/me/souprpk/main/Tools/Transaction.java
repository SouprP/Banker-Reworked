package me.souprpk.main.Tools;

import me.souprpk.main.Banker;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.awt.*;
import java.math.BigDecimal;
import java.util.UUID;

public class Transaction {

    private Banker banker = Banker.getMain();
    private UUID uuid;
    private BigDecimal amount;
    private TransactionType type;
    private BigDecimal minDiscordAmount = BigDecimal.valueOf(banker.getConfig().getDouble("discord.min-amount"));

    public enum TransactionType{
        Deposit,
        Withdraw;
    }
    public Transaction(UUID uuid, BigDecimal amount, TransactionType transactionType){
        this.uuid = uuid;
        this.amount = amount;
        this.type = transactionType;

        if(type.equals(TransactionType.Deposit)) handleDeposit();
        if(type.equals(TransactionType.Withdraw)) handleWithdraw();
    }

    private void handleDeposit(){
        if(amount.compareTo(minDiscordAmount) == 1)
            if(banker.discordHandle != null)
                banker.discordHandle.sendMessage(Bukkit.getPlayer(uuid), ChatColor.stripColor(Bukkit.getPlayer(uuid).getDisplayName()) +" deposited " + amount + "$.", true, Color.GRAY);

        playSound(Bukkit.getPlayer(uuid), TransactionType.Deposit);
        banker.logging.log(ChatColor.stripColor(Bukkit.getPlayer(uuid).getDisplayName()) +" deposited " + amount + "$.");
        if(banker.getConfig().getString("main.storage-system").equals("mysql")){
            banker.data.deposit(uuid, amount);
            return;
        }
        banker.flatData.deposit(uuid, amount);
        return;
    }

    private void handleWithdraw(){
        if(amount.compareTo(minDiscordAmount) == 1)
            if(banker.discordHandle != null)
                banker.discordHandle.sendMessage(Bukkit.getPlayer(uuid), ChatColor.stripColor(Bukkit.getPlayer(uuid).getDisplayName()) + " withdrew " + amount + "$.", true, Color.GRAY);

        playSound(Bukkit.getPlayer(uuid), TransactionType.Withdraw);
        banker.logging.log(ChatColor.stripColor(Bukkit.getPlayer(uuid).getDisplayName()) +" withdrew " + amount + "$.");
        if(banker.getConfig().getString("main.storage-system").equals("mysql")){
            banker.data.withdraw(uuid, amount);
            return;
        }
        banker.flatData.withdraw(uuid, amount);
        return;
    }

    private void playSound(Player player, TransactionType type){
        try {
            if(type.equals(TransactionType.Deposit)){
                Sound sound = Sound.valueOf(banker.getConfig().getString("main.deposit-sound"));
                player.playSound(player.getLocation(), sound, 1, 1);
            }
            if(type.equals(TransactionType.Withdraw)){
                Sound sound = Sound.valueOf(banker.getConfig().getString("main.withdraw-sound"));
                player.playSound(player.getLocation(), sound, 1, 1);
            }
        }catch (Exception ignored){

        }
    }
}
