package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountDao  {
    
    
        List<Account> findAllAccounts();
        
        Account findAccountByAccountId(long accountId);
        
        Account findAccountByUserId(long userId);
        
        Balance getBalance(String user);

        void updateAccount(Account account);
}
