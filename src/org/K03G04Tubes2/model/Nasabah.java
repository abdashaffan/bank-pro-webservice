package org.K03G04Tubes2.model;

import java.util.ArrayList;

public class Nasabah {
    private String name;
    private String accountNumber;
    private int balance;
    private ArrayList<Integer> virtualAccounts;
    private ArrayList<Transaction> transactionHistory;

    public Nasabah() {
    }
    public Nasabah (String name, String accountNumber) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = 0;
        this.virtualAccounts = new ArrayList<>();
        this.transactionHistory = new ArrayList<>();
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

    public ArrayList<Integer> getVirtualAccounts() {
        return this.virtualAccounts;
    }

    public void addTransactionHistory(Transaction t) {
        this.transactionHistory.add(t);
    }
    public void addTransactionHistory(Integer va) {
        this.virtualAccounts.add(va);

    }
}
