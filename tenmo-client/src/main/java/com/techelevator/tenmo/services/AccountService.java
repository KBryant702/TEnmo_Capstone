package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;

public interface AccountService {
    
    
    Account getBalance(AuthenticatedUser authenticatedUser);

    Account getAccountId(AuthenticatedUser authenticatedUser, long accountId);

    Account getAccountByUserId(AuthenticatedUser authenticatedUser, long userId);
    
    
    
}
