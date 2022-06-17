package com.techelevator.tenmo.model;

import org.springframework.data.annotation.Id;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long transferId;
    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "transfer_type_id")
    private long transferTypeId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transfer_status_id")
    private long transferStatusId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_from", referencedColumnName = "account_id")
    private long accountFrom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_to", referencedColumnName = "account_id")
    private long accountTo;
    @NotNull
    @PositiveOrZero
    private BigDecimal amount;

    public Transfer(){
        
    }
    
    
    public Transfer(long transferId, long transferTypeId, long transferStatusId, long accountFrom, long accountTo, BigDecimal amount) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
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

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferTypeId=" + transferTypeId +
                ", transferStatusId=" + transferStatusId +
                ", accountFrom=" + accountFrom +
                ", accountTo=" + accountTo +
                ", amount=" + amount +
                '}';
    }
}
