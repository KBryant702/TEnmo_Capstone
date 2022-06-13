package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Balance {

    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "balance=" + balance +
                '}';
    }
}
