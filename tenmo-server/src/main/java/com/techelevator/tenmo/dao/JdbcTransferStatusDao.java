package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JdbcTransferStatusDao implements TransferStatusDao{
    
    private JdbcTemplate jdbcTemplate;
    
    public JdbcTransferStatusDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Override
    public TransferStatus getByStatusDesc(String desc) {
        TransferStatus transferStatus = null;
        // select * in transfer_status and search by transfer_status_desc
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status WHERE transfer_status_desc = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, desc);
        if (result.next()) {
            int transferStatusId = result.getInt("transfer_status_id");
            String transferStatusDesc = result.getString("transfer_status_desc");
            transferStatus = new TransferStatus(transferStatusId, transferStatusDesc);
        }
        return transferStatus;
    }

    @Override
    public TransferStatus getByStatusId(long statusId) {
        // select * in transfer_status and search by transfer_status_id
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status WHERE transfer_status_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, statusId);
        TransferStatus transferStatus = null;
        if(result.next()) {
            int transferStatusId2 = result.getInt("transfer_status_id");
            String transferStatusDesc = result.getString(("transfer_status_desc"));
            transferStatus = new TransferStatus(transferStatusId2, transferStatusDesc);
        }
        return transferStatus;
    }
}
