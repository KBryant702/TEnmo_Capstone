package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;

public interface TransferService {
    
    void createTransfer(AuthenticatedUser authenticatedUser, Transfer transfer);
    
    Transfer[] getTransfersByUserId(AuthenticatedUser authenticatedUser, long userId);
    
    Transfer getTransferByTransferId(AuthenticatedUser authenticatedUser, long transferId);
    
    Transfer[] getAllTransfers(AuthenticatedUser authenticatedUser);     // revisit
    
    //Transfer[] getPendingTransferByUserId(AuthenticatedUser authenticatedUser, long userId);

    Transfer[] getPendingTransfersByUserId(AuthenticatedUser authenticatedUser);
    
    void updateTransfer(AuthenticatedUser authenticatedUser, Transfer transfer);
    
    
}
