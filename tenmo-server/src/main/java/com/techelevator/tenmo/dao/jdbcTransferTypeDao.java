package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class jdbcTransferTypeDao implements TransferTypeDao{
    
    private JdbcTemplate jdbcTemplate;
    
    public jdbcTransferTypeDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Override
    public TransferType getTransferTypeByTypeId(long typeId){
        TransferType transferType = null;
        String sql = "SELECT transfer_type_id, transfer_type_desc FROM transfer_type WHERE transfer_type_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, typeId);
        
        if(result.next()){
            long transferTypeId = result.getLong("transfer_type_id");
            String transferTypeDesc = result.getString("transfer_type_desc");
            transferType = new TransferType(transferTypeId, transferTypeDesc);
        }
        return transferType;
    }
    
    @Override
    public TransferType getTransferTypeByTypeDesc(String typeDesc){
        TransferType transferType = null;
        String sql = "SELECT transfer_type_id, transfer_type_desc FROM transfer_type WHERE transfer_type_desc = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, typeDesc);
        
        if(result.next()){
            long transferTypeId = result.getLong("transfer_type_id");
            String transferTypeDesc = result.getString("transfer_type_desc");
            transferType = new TransferType(transferTypeId, transferTypeDesc);
        }
        return transferType;
    }
    
}
