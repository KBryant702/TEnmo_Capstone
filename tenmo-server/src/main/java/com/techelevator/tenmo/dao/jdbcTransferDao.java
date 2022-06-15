package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class jdbcTransferDao implements TransferDao{
    
    private JdbcTemplate jdbcTemplate;

    public jdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public List<Transfer> getAllTransfers(){
        List<Transfer> transfers = new ArrayList<>();
        
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer JOIN transfer_status USING(transfer_status_id) " +
                "JOIN transfer_type USING(transfer_type_id);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()){
            transfers.add(mapResultToAccounts(results));
        }
        return transfers;
    }
    
    @Override
    public List<Transfer> getTransferByUserId(long userId){
        List<Transfer> transfers = new ArrayList<>();
        
        String sql = "SELECT userId, transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount, " +
                "From transfer JOIN account ON account.account_id = transfer.account_to WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while(results.next()){
            transfers.add(mapResultToAccounts(results));
        }
        return transfers;
    }
    
    @Override
    public Transfer getTransferByTransferId(long transferId){
        Transfer transfer = null;
        
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "From transfer WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if(results.next()){
            transfer = mapResultToAccounts(results);
        }
        return transfer;
    }
    
    @Override
    public List<Transfer> getPendingTransfers(long userId){
        List<Transfer> transfers = new ArrayList<>();
        
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "From transfer JOIN account ON account.account_id = transfer.account_to " +
                "JOIN transfer_status USING(transfer_status_id) WHERE user_id = ? AND transfer_status_desc = 'Pending';";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while(results.next()){
            transfers.add(mapResultToAccounts(results));
        }
        return transfers;
    }
    
    @Override
    public boolean createTransfer(Transfer transfer){
        // boolean success;               // do we need to return an actual boolean variable or can the current return function work?
        String sql = "INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?,?,?,?,?,?);";
        int numberOfRows = jdbcTemplate.update(sql, transfer.getTransferId(), transfer.getTransferTypeId(), transfer.getTransferStatusId(), 
                transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        return numberOfRows == 1;
    }
    
    @Override
    public boolean updateTransfer(Transfer transfer){
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
        int numberOfRows = jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());
        
        return numberOfRows == 1;
    }


    private Transfer mapResultToAccounts(SqlRowSet result){
        long transferId = result.getLong("transfer_id");
        long transferTypeId = result.getLong("transfer_type_id");
        long transferStatusId = result.getLong("transfer_status_id");
        long accountFrom = result.getLong("account_from");
        long accountTo = result.getLong("account_to");
        BigDecimal amount = result.getBigDecimal("amount");
        return new Transfer(transferId, transferTypeId, transferStatusId, accountFrom, accountTo, amount);
    }
    
}
