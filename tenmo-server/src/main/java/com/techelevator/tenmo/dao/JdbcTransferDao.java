package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
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
    public List<Transfer> getTransfersByUserAccountId(long userAccountId){
        List<Transfer> transfers = new ArrayList<>();
        
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer JOIN account ON account.account_id = transfer.account_to WHERE account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userAccountId);
        while(results.next()){
            transfers.add(mapResultToAccounts(results));
        }
        return transfers;
    }
    
    @Override
    public Transfer getTransferByTransferId(long transferId){
        Transfer transfer = null;
        
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if(results.next()){
            transfer = mapResultToAccounts(results);
        }
        return transfer;
    }
    
//    @Override
//    public List<Transfer> getPendingTransfers(long userId){
//        List<Transfer> transfers = new ArrayList<>();
//        
//        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
//                "FROM transfer JOIN account ON account.account_id = transfer.account_to " +
//                "JOIN transfer_status USING(transfer_status_id) WHERE user_id = ? AND transfer_status_desc = 'Pending';";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
//        while(results.next()){
//            transfers.add(mapResultToAccounts(results));
//        }
//        return transfers;
//    }

    @Override
    public void createTransfer(Transfer transfer){
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?,?,?,?,?);";
       jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

//    // test method
//    @Override
//    public void createTransfer(Transfer transfer){
//        String sql = "INSERT INTO transfer (account_from, account_to, amount) " +
//                "VALUES (?,?,?);";
//        jdbcTemplate.update(sql, transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
//    }

//    private long getNextIdFromSequence() {
//        SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_transfer_id'); ");
//        if(nextIdResult.next()) {
//            return nextIdResult.getLong(1);
//        } else {
//            throw new RuntimeException("Something went wrong finding next transfer_id sequence");
//        }
//    }
    
//    @Override
//    public void updateTransfer(Transfer transfer){
//        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
//        jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());
//    }


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
