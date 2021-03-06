package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.exceptions.InsufficientFunds;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Balance getBalance(Principal principal) {
        System.out.println(principal.getName());
        return accountDao.getBalance(principal.getName());
    }

    @GetMapping(path = "/tenmo_user")
    public List<User> getUsers() {
        return userDao.findAll();
    }

    @GetMapping(path = "/transfer")
    public List<Transfer> getAllTransfers() {
        return transferDao.getAllTransfers();
    }

    @GetMapping(path = "/transfer_type/filter")
    public TransferType getTransferTypeByTypeDesc(@RequestParam String desc) {
        return transferTypeDao.getTransferTypeByTypeDesc(desc);
    }

    @GetMapping(path = "/transfer_status/filter")
    public TransferStatus getTransferStatusByDesc(@RequestParam String desc) {
        return transferStatusDao.getByStatusDesc(desc);
    }

    @GetMapping(path = "/transfer_status/{id}")
    public TransferStatus getTransferStatusFromId(@PathVariable long transferStatusId) {
        return transferStatusDao.getByStatusId(transferStatusId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/transfer/")
    public void createTransfer(@RequestBody Transfer transfer) throws InsufficientFunds {
        BigDecimal transferAmount = transfer.getAmount();
        Account accountFrom = accountDao.findAccountByAccountId(transfer.getAccountFrom());
        Account accountTo = accountDao.findAccountByAccountId(transfer.getAccountTo());
        accountFrom.getBalance().sendMoney(transferAmount);
        accountTo.getBalance().receiveMoney(transferAmount);
        // create transfer and update balances
        transferDao.createTransfer(transfer);
        accountDao.updateAccount(accountFrom);
        accountDao.updateAccount(accountTo);
    }

    @GetMapping(path = "/account/{userId}")
    public Account findAccountByUserId(@PathVariable long userId) {        
        return accountDao.findAccountByUserId(userId);
    }

    @GetMapping(path = "/transfer/tenmo_user/{userAccountId}")
    public List<Transfer> getTransfersByUserAccountId(@PathVariable long userAccountId) {
        return transferDao.getTransfersByUserAccountId(userAccountId);
    }

    @GetMapping(path = "/tenmo_user/{userId}")
    public User getUserByUserId(@PathVariable long userId) {
        return userDao.getUserByUserId(userId);
    }
    
    @GetMapping(path = "/transfer/{transferId}")
    public Transfer transferDetails(@PathVariable long transferId){
        return transferDao.transferDetails(transferId);
    }
}
