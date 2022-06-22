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
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;
    
    public JdbcAccountDao(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<Account> findAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        // select * in the account table
        String sql = "SELECT account_id, user_id, balance FROM account;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        while (results.next()) {
            accounts.add(mapResultToAccounts(results));
        }
        return accounts;
    }

    @Override
    public Account findAccountByAccountId(long accountId) {
        Account account = null;
        // select * in the account table searching for account_id 
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);

        if (results.next()) {
            account = mapResultToAccounts(results);
        }
        return account;
    }

    @Override
    public Account findAccountByUserId(long userId) {
        Account account = null;
        // select user_id and * in the account table, joining on the tenmo_user table to search by user_id
        String sql = "SELECT user_id, account_id, balance FROM account JOIN tenmo_user USING(user_id) WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        if (results.next()) {
            account = mapResultToAccounts(results);
        }
        return account;
    }

    @Override
    public Balance getBalance(String user) {     // this needs to return current logged in account balance
        Balance balance = new Balance();
        // select user_id and * from account, joining on the tenmo_user table to search by username
        String sql = "SELECT account_id, user_id, balance FROM account JOIN tenmo_user USING(user_id) WHERE username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user);
        if (results.next()) {
            String accountBalance = results.getString("balance");
            balance.setBalance(new BigDecimal(accountBalance));
        }
        return balance;
    }

    @Override
    public void updateAccount(Account account){
        // update account balance per supplied account_id
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, account.getBalance().getBalance(), account.getAccountId());
    }

    private Account mapResultToAccounts(SqlRowSet result) {
        long accountId = result.getLong("account_id");
        long userId = result.getLong("user_id");
        Balance balance = new Balance();
        String accountBalance = result.getString("balance");
        balance.setBalance(new BigDecimal(accountBalance));
        return new Account(accountId, userId, balance);
    }
}
