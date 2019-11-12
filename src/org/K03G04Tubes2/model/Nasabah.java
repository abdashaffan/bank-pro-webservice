package org.K03G04Tubes2.model;

import java.util.ArrayList;

public class Nasabah {
    private String name;
    private String accountNumber;
    private int balance;
    private ArrayList<Transaction> transactionHistory;

    public Nasabah() {
    }
    public Nasabah (String name, String accountNumber, int balance,ArrayList<Transaction> transactionHistory) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>(transactionHistory);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public ArrayList<Transaction> getTransactionHistory() {
        return this.transactionHistory;
    }

    public void addTransactionHistory(Transaction t) {
        this.transactionHistory.add(t);

    }
}
