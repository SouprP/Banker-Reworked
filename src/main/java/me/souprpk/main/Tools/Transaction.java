package me.souprpk.main.Tools;

import java.math.BigDecimal;

public class Transaction {

    public enum TransactionType{
        Deposit,
        Withdraw;
    }
    public Transaction(BigDecimal amount, TransactionType transactionType){
        if(transactionType.equals(TransactionType.Deposit)){

        }
    }
}
