package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    
    List<Transfer> getAllTransfers();
    
    List<Transfer> getTransferByUserId(long userId);

    Transfer getTransferByTransferId(long transferId);
    
    List<Transfer> getPendingTransfers(long userid);
    
    boolean createSendTransfer(long accountFrom, long accountTo, BigDecimal amount);
    
    boolean createRequestTransfer(long accountFrom, long accountTo, BigDecimal amount);
    
    boolean updateTransfer(Transfer transferToUpdate);
    
}
