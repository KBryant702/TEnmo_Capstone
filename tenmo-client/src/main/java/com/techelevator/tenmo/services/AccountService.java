package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Balance;

public interface AccountService {
    
    
    Balance getBalance(AuthenticatedUser authenticatedUser);

    Account getAccountById(AuthenticatedUser authenticatedUser, long accountId);

    Account getAccountByUserId(AuthenticatedUser authenticatedUser, long userId);
    
    
    
}
