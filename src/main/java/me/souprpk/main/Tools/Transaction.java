package me.souprpk.main.Tools;

import me.souprpk.main.Banker;

import java.math.BigDecimal;
import java.util.UUID;

public class Transaction {

    private Banker banker = Banker.getMain();
    private UUID uuid;
    private BigDecimal amount;
    private TransactionType type;

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
        if(banker.mySQL.isConnected()){
            banker.data.deposit(uuid, amount);
            return;
        }
        banker.flatData.deposit(uuid, amount);
        return;
    }

    private void handleWithdraw(){
        if(banker.mySQL.isConnected()){
            banker.data.withdraw(uuid, amount);
            return;
        }
        banker.flatData.withdraw(uuid, amount);
        return;
    }
}
