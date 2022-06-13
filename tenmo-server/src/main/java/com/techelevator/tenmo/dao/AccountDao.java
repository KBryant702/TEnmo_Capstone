package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;

import java.util.List;

public interface AccountDao {
    
    
        List<Account> findAllAccounts();
        
        Account findAccountById(long accountId);
        
        Account findAccountByUserId(long userId);
        
        Balance getBalance(String user);
        
        boolean createAccount(long accountId, long userId, Balance balance);
        
        boolean updateAccount(Account accountToUpdate);
        
     
    
}
