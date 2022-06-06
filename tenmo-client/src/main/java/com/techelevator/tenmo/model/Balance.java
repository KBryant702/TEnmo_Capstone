package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Balance extends Account {
    
    BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
