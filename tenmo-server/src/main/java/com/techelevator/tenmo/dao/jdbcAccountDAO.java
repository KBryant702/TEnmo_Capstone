package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class jdbcAccountDAO implements AccountDao{
    
    private JdbcTemplate jdbcTemplate;
    
    public void JdbcAccountDAO(DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
    }
    
    @Override
    public List<Account> findAllAccounts(){
        List<Account> accounts = new ArrayList<>();
        
        String sql = "SELECT account_id FROM account;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        
        while(results.next()){
            accounts.add(mapResultToAccounts(results));
        }
        return accounts;
    }
    
    @Override
    public Account findAccountByAccountId(long accountId){
        Account account = null;
        String sql = "SELECT account_id FROM account WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        
        if(results.next()){
            account = mapResultToAccounts(results);
        }        
        return account;
    }
    
    @Override
    public Account findAccountByUserId(long userId){
        Account account = null;
        String sql = "SELECT user_id, account_id FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        if(results.next()){
            account = mapResultToAccounts(results);
        }
        return account;
    }
    
    @Override
    public Balance getBalance(String user){     // this needs to return current logged in account balance
        Balance balance = new Balance();
        String sql = "SELECT username, balance FROM account JOIN users USING(user_id) WHERE username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user);
        if(results.next()){
            String accountBalance = results.getString("balance");
            balance.setBalance(new BigDecimal(accountBalance));
        }
        return balance;
    }
    
    // add an update balance method
    
    private Account mapResultToAccounts(SqlRowSet result){
        long accountId = result.getLong("account_id");
        long userId = result.getLong("user_id");
        Balance balance = new Balance();
        String accountBalance = result.getString("balance");
        balance.setBalance(new BigDecimal(accountBalance));
        return new Account(accountId, userId, balance);
    }
    
}
