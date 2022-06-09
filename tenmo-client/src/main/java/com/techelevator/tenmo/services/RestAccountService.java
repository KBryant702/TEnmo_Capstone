package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import com.techelevator.tenmo.model.AuthenticatedUser;


@Component
public class RestAccountService implements AccountService {

    private RestTemplate restTemplate;
    public static final String API_BASE_URL = "http://localhost:5432/tenmo"; // insert final url here


    @Override
    public Account getBalance(AuthenticatedUser authenticatedUser) {
        Account balance = null;
        try{
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + authenticatedUser, HttpMethod.GET, createHttpEntity(authenticatedUser), Account.class);
            balance = response.getBody();
        }catch(RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    //add balanceUpdate?

    @Override
    public Account getAccountId(AuthenticatedUser authenticatedUser, long accountId) {
        Account account = null;
        try{
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + accountId, HttpMethod.GET, createHttpEntity(authenticatedUser), Account.class);
            account = response.getBody(); 
        }catch(RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    @Override
    public Account getAccountByUserId(AuthenticatedUser authenticatedUser, long userId) {
        Account account = null;
        try{
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + userId, HttpMethod.GET, createHttpEntity(authenticatedUser), Account.class);
            account = response.getBody(); 
        }catch(RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        
        return account;
    }

    private HttpEntity<AuthenticatedUser> createHttpEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(authenticatedUser, headers);
    }
    
    
}
