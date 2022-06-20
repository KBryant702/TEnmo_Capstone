package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    
    List<Transfer> getAllTransfers();
    
    List<Transfer> getTransfersByUserAccountId(long userAccountId);

    Transfer getTransferByTransferId(long transferId);
    
//    List<Transfer> getPendingTransfers(long userid);
    
    void createTransfer(Transfer transfer);
    
//    void updateTransfer(Transfer transferToUpdate);
    
}
