package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface TransferService {
    
    boolean createTransfer(AuthenticatedUser authenticatedUser, Transfer transfer);
    
    Transfer[] getTransfersByUserId(AuthenticatedUser authenticatedUser, long userId);
    
    Transfer getTransferByTransferId(AuthenticatedUser authenticatedUser, long transferId);
    
    Transfer[] getAllTransfers(AuthenticatedUser authenticatedUser);     // revisit
    
    //Transfer[] getPendingTransferByUserId(AuthenticatedUser authenticatedUser, long userId);

    Transfer[] getPendingTransfersByUserId(AuthenticatedUser authenticatedUser);
    
    void updateTransfer(AuthenticatedUser authenticatedUser, Transfer transfer);
    
    
}
