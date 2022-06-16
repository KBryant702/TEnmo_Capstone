package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.exceptions.InsufficientFunds;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TEnmoController {
    
    @Autowired
    AccountDao accountDao;
    
    @Autowired
    UserDao userDao;
    
    @Autowired
    TransferDao transferDao;
    
    @Autowired
    TransferTypeDao transferTypeDao;
    
    @Autowired
    TransferStatusDao transferStatusDao;
    
    @GetMapping(path = "/balance")
    public Balance getBalance(Principal principal){
        System.out.println(principal.getName());
        return accountDao.getBalance(principal.getName());
    }
    
    @GetMapping(path = "/tenmo_user")
    public List<User> getUsers(){
        return userDao.findAll();
    }

    @GetMapping(path="/transfers")
    public List<Transfer> getAllTransfers() {
        return transferDao.getAllTransfers();
    }

    @GetMapping(path="/transferType/filter")
    public TransferType getTransferTypeByTypeDesc(@RequestParam String desc) {
        return transferTypeDao.getTransferTypeByTypeDesc(desc);
    }
    
    @GetMapping(path = "/transferStatus/filter")
    public TransferStatus getTransferStatusByDesc(@RequestParam String desc){
        return transferStatusDao.getByStatusDesc(desc);
    }

    @GetMapping(path="/transferstatus/{id}")
    public TransferStatus getTransferStatusFromId(@PathVariable long transferStatusId) {
        return transferStatusDao.getByStatusId(transferStatusId);
    }

    @GetMapping(path="/transfers/tenmo_user/{userId}/pending")
    public List<Transfer> getPendingTransfersByUserId(@PathVariable long userId) {
        return transferDao.getPendingTransfers(userId);
    }

    @PostMapping(path = "/transfer/{id}")
    public void createTransfer(@RequestBody Transfer transfer, @PathVariable long id) throws InsufficientFunds {
        BigDecimal transferAmount = transfer.getAmount();
        Account accountFrom = accountDao.findAccountByAccountId(transfer.getAccountFrom());
        Account accountTo = accountDao.findAccountByAccountId(transfer.getAccountTo());

        accountFrom.getBalance().sendMoney(transferAmount);
        accountTo.getBalance().receiveMoney(transferAmount);
        transferDao.createTransfer(transfer);
        accountDao.updateAccount(accountFrom);
        accountDao.updateAccount(accountTo);
    }

    @GetMapping(path="/transfers")
    public void updateTransferStatus(@RequestBody Transfer transfer) throws InsufficientFunds {
        if(transfer.getTransferStatusId() == transferStatusDao.getByStatusDesc("Approved").getTransferStatusId()) {
            BigDecimal amountToTransfer = transfer.getAmount();
            Account accountFrom = accountDao.findAccountByAccountId(transfer.getAccountFrom());
            Account accountTo = accountDao.findAccountByAccountId(transfer.getAccountTo());

            accountFrom.getBalance().sendMoney(amountToTransfer);
            accountTo.getBalance().receiveMoney(amountToTransfer);
            transferDao.updateTransfer(transfer);
            accountDao.updateAccount(accountFrom);
            accountDao.updateAccount(accountTo);
        } else {
            transferDao.updateTransfer(transfer);
        }
    }
    
    @GetMapping(path = "/account/{userId}")
    public Account findAccountByUserId(@PathVariable long userId){
        return accountDao.findAccountByUserId(userId);
    }
    
    @GetMapping(path = "/account/{accountId}")
    public Account findAccountByAccountId(@PathVariable long accountId){
        return accountDao.findAccountByAccountId(accountId);
    }
    
    @GetMapping(path = "/transfer/tenmo_user/{userId}")
    public List<Transfer> getTransfersByUserId(@PathVariable long userId){
        return transferDao.getTransfersByUserId(userId);
    }
    
    @GetMapping(path = "/tenmo_user/{userId}")
    public User getUserByUserId(@PathVariable long userId){
        return userDao.getUserByUserId(userId);
    }
}
