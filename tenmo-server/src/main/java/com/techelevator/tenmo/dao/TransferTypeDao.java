package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferType;

public interface TransferTypeDao {
    
    TransferType getByTypeId(long typeId);
    
    TransferType getByTypeDesc(String typeDesc);
    
}
