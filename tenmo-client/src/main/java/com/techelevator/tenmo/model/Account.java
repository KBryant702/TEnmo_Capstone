package com.techelevator.tenmo.model;

public class Account {

    private long accountId;
    private long userId;
    private Balance balance;

//    public Account(){
//
//    }
//
//    public Account(long accountId, long userId, Balance balance) {
//        this.accountId = accountId;
//        this.userId = userId;
//        this.balance = balance;
//    }

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
}
