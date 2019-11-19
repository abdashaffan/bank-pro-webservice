package org.K03G04Tubes2.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class Nasabah {
    private String name;
    private int accountNumber;
    private int balance;
    private ArrayList<Integer> virtualAccounts;
    private ArrayList<Transaction> transactionHistory;

    public Nasabah() {
    }
    public Nasabah (String name, int accountNumber) {
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

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @XmlElementWrapper(name="transactions_history")
    @XmlElement(name="transaction")
    public ArrayList<Transaction> getTransactionHistory() {
        return this.transactionHistory;
    }

    @XmlElementWrapper(name="virtual_accounts")
    @XmlElement(name="account")
    public ArrayList<Integer> getVirtualAccounts() {
        return this.virtualAccounts;
    }

    public void addTransactionHistory(Transaction t) {
        this.transactionHistory.add(t);
    }
    public void addVirtualAccount(Integer va) {
        this.virtualAccounts.add(va);

    }
}
