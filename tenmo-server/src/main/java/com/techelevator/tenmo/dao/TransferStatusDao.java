package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;

public interface TransferStatusDao {
    
    TransferStatus getByStatusDesc(String desc);
    
    TransferStatus getByStatusId(long statusId);
    
}
