package com.techelevator.tenmo.model;

public class TransferType {

    private long transferTypeId;
    private String transferTypeDesc;
    
    public long getTransferTypeById() {
        return transferTypeId;
    }
    public void setTransferTypeById(long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }
    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }
    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }
}
