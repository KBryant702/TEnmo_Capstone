package com.techelevator.tenmo.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

public class Account {

    @Id
    @NotNull
    private long accountId;

    @NotNull
    private long userId;
    
    @NotNull
    private Balance balance;


    public void setId(Long id) {
        this.accountId = id;
    }

    public Account(){

    }

    public Account(long accountId, long userId, Balance balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public long getAccountId() {
        return accountId;
    }
    public long getUserId() {
        return userId;
    }
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public Balance getBalance() {
        return balance;
    }
    public void setBalance(Balance balance) {
        this.balance = balance;
    }


    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", balance=" + balance +
                '}';
    }
}
