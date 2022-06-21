package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private long transferId;
    private long transferTypeId;
    private long transferStatusId;
    private long accountFrom;
    private long accountTo;
    private BigDecimal amount;
    private String userTo;
    private String userFrom;
    private TransferType transferType;
    private TransferStatus TransferStatus;


    public TransferType getTransferType() {
        return transferType;
    }
    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }
    public com.techelevator.tenmo.model.TransferStatus getTransferStatus() {
        return TransferStatus;
    }
    public void setTransferStatus(com.techelevator.tenmo.model.TransferStatus transferStatus) {
        TransferStatus = transferStatus;
    }
    public String getUserTo() {
        return userTo;
    }
    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }
    public String getUserFrom() {
        return userFrom;
    }
    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }
    public long getTransferTypeId() {
        return transferTypeId;
    }
    public void setTransferTypeId(long transferTypeId) {
        this.transferTypeId = transferTypeId;
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

}
