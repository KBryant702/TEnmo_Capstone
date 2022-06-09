package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    
    private long accountId;
    private long userId;
    private BigDecimal balance;
    
    public Account(){
        
    }
    
    public Account(long accountId, long userId, BigDecimal balance) {
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
