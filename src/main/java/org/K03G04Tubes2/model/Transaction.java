package org.K03G04Tubes2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Person")
public class Transaction {
    private Date date;
    private String transactionType;
    private int amount;
    private int sender;
    private int receiver;

    public Transaction() {

    }

    public Transaction(String transactionType, int amount, int sender, int receiver, Date date) {
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

    public int getSender() {
        return sender;
    }

    public int getReceiver() {
        return receiver;
    }
}
