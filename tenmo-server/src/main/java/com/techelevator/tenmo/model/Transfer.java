package com.techelevator.tenmo.model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Transfer {
    private long transferId;
    private long transferTypeId;
    private long transferStatusId;
    private long accountFrom;
    private long accountTo;
    private BigDecimal amount;
    private String userFrom;
    private String userTo;
    private String transferType;
    private String transferStatus;

    public Transfer(){
        
    }
    
    
    public Transfer(long transferId, long transferTypeId, long transferStatusId, long accountFrom, long accountTo, 
                    BigDecimal amount, String userFrom, String userTo, String transferType, String transferStatus) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.transferStatus = transferStatus;
        this.transferType = transferType;
    }

    public long getTransferId() {
        return transferId;
    }
    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }
    public long getTransferStatusId() {
        return transferStatusId;
    }
    public void setTransferStatusId(long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }
    public long getAccountFrom() {
        return accountFrom;
    }
    public long getAccountTo() {
        return accountTo;
    }
    public void setAccountFrom(long accountFrom) {
        this.accountFrom = accountFrom;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAccountTo(long accountTo) {
        this.accountTo = accountTo;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public long getTransferTypeId() {
        return transferTypeId;
    }
    public void setTransferTypeId(long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }
    public String getUserFrom() {
        return userFrom;
    }
    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }
    public String getUserTo() {
        return userTo;
    }
    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferTypeId=" + transferTypeId +
                ", transferStatusId=" + transferStatusId +
                ", accountFrom=" + accountFrom +
                ", accountTo=" + accountTo +
                ", amount=" + amount +
                ", userFrom=" + userFrom +
                ", userTo=" + userTo +
                '}';
    }
}
