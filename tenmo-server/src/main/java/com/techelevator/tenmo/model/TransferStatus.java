package com.techelevator.tenmo.model;

import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TransferStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long transferStatusId;
    @NotEmpty
    private String transferStatusDesc;

    public TransferStatus(){
        
    }
    
    public TransferStatus(long transferStatusId, String transferStatusDesc) {
        this.transferStatusId = transferStatusId;
        this.transferStatusDesc = transferStatusDesc;
    }

    public long getTransferStatusId() {
        return transferStatusId;
    }
    public void setTransferStatusId(long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }
    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }
    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }

    @Override
    public String toString() {
        return "TransferStatus{" +
                "transferStatusId=" + transferStatusId +
                ", transferStatusDesc='" + transferStatusDesc + '\'' +
                '}';
    }
}
