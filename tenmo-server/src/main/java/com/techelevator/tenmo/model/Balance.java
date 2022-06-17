package com.techelevator.tenmo.model;

import com.techelevator.tenmo.exceptions.InsufficientFunds;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class Balance {
    @NotNull
    @PositiveOrZero
    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void sendMoney(BigDecimal amount) throws InsufficientFunds{
        BigDecimal newBalance = new BigDecimal(String.valueOf(balance)).subtract(amount);
        if(newBalance.compareTo(BigDecimal.ZERO) >= 0){
            this.balance = newBalance;
        }else{
            throw new InsufficientFunds();
        }
    }
    
    public void receiveMoney(BigDecimal amount){
        this.balance = new BigDecimal(String.valueOf(balance)).add(amount);
    }
    
    
    @Override
    public String toString() {
        return "Balance{" +
                "balance=" + balance +
                '}';
    }
}
