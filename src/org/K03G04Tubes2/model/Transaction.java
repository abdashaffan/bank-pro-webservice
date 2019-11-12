package org.K03G04Tubes2.model;
import java.util.Date;

public class Transaction {
    private Date date;
    private String transactionType;
    private int amount;
    private String sender;
    private String receiver;

    public Transaction() {

    }

    public Transaction(String transactionType, int amount, String sender, String receiver, Date date) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public int getAmount() {
        return amount;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }
}
