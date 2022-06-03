package me.souprpk.main.Tools;

import me.souprpk.main.Banker;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

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
        Withdraw,
        LoanTake,
        DebtPay;
    }
    public Transaction(UUID uuid, BigDecimal amount, TransactionType transactionType){
        this.uuid = uuid;
        this.amount = amount;
        this.type = transactionType;

        if(type.equals(TransactionType.Deposit)) handleDeposit();
        if(type.equals(TransactionType.Withdraw)) handleWithdraw();
    }

    private void handleDeposit(){
        Utilities utils = new Utilities(banker);
        if(amount.compareTo(minDiscordAmount) == 1)
            if(banker.discordHandle != null)
                banker.discordHandle.sendMessage(Bukkit.getPlayer(uuid), ChatColor.stripColor(Bukkit.getPlayer(uuid).getDisplayName()) +" deposited " + utils.truncateDecimal(amount, 2) + "$.", true, Color.GRAY);

        utils.playSound(Bukkit.getPlayer(uuid), TransactionType.Deposit);
        banker.logging.log(ChatColor.stripColor(Bukkit.getPlayer(uuid).getDisplayName()) +" deposited " + utils.truncateDecimal(amount, 2) + "$.");
        if(banker.getConfig().getString("main.storage-system").equals("mysql")){
            banker.data.deposit(uuid, amount);
            return;
        }
        banker.flatData.deposit(uuid, amount);
    }

    private void handleWithdraw(){
        Utilities utils = new Utilities(banker);
        if(amount.compareTo(minDiscordAmount) == 1)
            if(banker.discordHandle != null)
                banker.discordHandle.sendMessage(Bukkit.getPlayer(uuid), ChatColor.stripColor(Bukkit.getPlayer(uuid).getDisplayName()) + " withdrew " + utils.truncateDecimal(amount, 2) + "$.", true, Color.GRAY);

        utils.playSound(Bukkit.getPlayer(uuid), TransactionType.Withdraw);
        banker.logging.log(ChatColor.stripColor(Bukkit.getPlayer(uuid).getDisplayName()) +" withdrew " + utils.truncateDecimal(amount, 2) + "$.");
        if(banker.getConfig().getString("main.storage-system").equals("mysql")){
            banker.data.withdraw(uuid, amount);
            return;
        }
        banker.flatData.withdraw(uuid, amount);
    }

}
