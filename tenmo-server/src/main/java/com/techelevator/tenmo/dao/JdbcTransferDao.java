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
    
    // TODO can rework SQL statement to omit the joins since everything needed is on the transfer table.
    @Override
    public List<Transfer> getAllTransfers(){
        List<Transfer> transfers = new ArrayList<>();
        // select * from the transfer table
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
        // need to set up join statements to reach across all tables needed for the parameters in displaying transfers 
        // made by the user, passing a limiter on the user_id to and from to differentiate sent or received
        String sql = "SELECT t.*, u.username AS userFrom, v.username AS userTo, ts.transfer_status_desc, tt.transfer_type_desc FROM transfer t " +
                "JOIN account a ON t.account_from = a.account_id " +
                "JOIN account b ON t.account_to = b.account_id " +
                "JOIN tenmo_user u ON a.user_id = u.user_id " +
                "JOIN tenmo_user v ON b.user_id = v.user_id " +
                "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
                "JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id " +
                "WHERE b.user_id = ? OR a.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userAccountId, userAccountId);
        while(results.next()){
            transfers.add(mapResultToAccounts(results));
        }
        return transfers;
    }
    
    @Override
    public Transfer transferDetails(long transferId){
        Transfer transfer = null;
        // setup join statements similar to the getTransfersByUserId method above (need to refactor name) to get all 
        // required details of a transfer, but instead, pass limiter to transfer_id
        String sql = "SELECT t.*, u.username AS userFrom, v.username AS userTo, transfer_type_desc, transfer_status_desc FROM transfer t " +
                "JOIN account a ON t.account_from = a.account_id " +
                "JOIN account b ON t.account_to = b.account_id " +
                "JOIN tenmo_user u ON a.user_id = u.user_id " +
                "JOIN tenmo_user v ON b.user_id = v.user_id " +
                "JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id " +
                "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
                "WHERE t.transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if(results.next()){
            transfer = mapResultToAccounts(results);
        }
        return transfer;
    }
    
    @Override
    public Transfer getTransferByTransferId(long transferId){
        Transfer transfer = null;
        // select * in transfer table and search by transfer_id
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if(results.next()){
            transfer = mapResultToAccounts(results);
        }
        return transfer;
    }
    
    @Override
    public void createTransfer(Transfer transfer){
        //create transfer SQL
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?,?,?,?,?);";
       jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    // make sure to account for all parameters needed on a transfer: transfer_id, transfer_type_id, transfer_status_id, 
    // account_from, account_to, amount, userFrom, userTo, transfer_type_desc, transfer_status_desc
    private Transfer mapResultToAccounts(SqlRowSet result){
        long transferId = result.getLong("transfer_id");
        long transferTypeId = result.getLong("transfer_type_id");
        long transferStatusId = result.getLong("transfer_status_id");
        long accountFrom = result.getLong("account_from");
        long accountTo = result.getLong("account_to");
        BigDecimal amount = result.getBigDecimal("amount");
        String userFrom = result.getString("userFrom");
        String userTo = result.getString("userTo");
        String transferType = result.getString("transfer_type_desc");
        String transferStatus = result.getString("transfer_status_desc");
        return new Transfer(transferId, transferTypeId, transferStatusId, accountFrom, accountTo, amount, userFrom, userTo, transferType, transferStatus);
    }
    
}
