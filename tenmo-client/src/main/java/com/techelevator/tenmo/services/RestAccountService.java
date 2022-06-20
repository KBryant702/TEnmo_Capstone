package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
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
    private final String API_BASE_URL;

    public RestAccountService(String API_BASE_URL) {
        this.restTemplate = new RestTemplate();
        this.API_BASE_URL = API_BASE_URL;
    }


    @Override
    public Balance getBalance(AuthenticatedUser authenticatedUser) {
        Balance balance = null;
        HttpEntity<AuthenticatedUser> entity = createHttpEntity(authenticatedUser);
        try{
//            ResponseEntity<Balance> response = restTemplate.exchange(API_BASE_URL + authenticatedUser, HttpMethod.GET, entity, Balance.class);        // authenticated user not needed here. replace with "/balance"
//            balance = response.getBody();
            ResponseEntity<Balance> response = restTemplate.exchange(API_BASE_URL + "balance", HttpMethod.GET, entity, Balance.class);     // works
            balance = response.getBody();
//            balance = restTemplate.exchange(API_BASE_URL + "/balance", HttpMethod.GET, entity, Balance.class).getBody();
        }catch(RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }


    @Override
    public Account getAccountById(AuthenticatedUser authenticatedUser, long accountId) {
        Account account = null;
        HttpEntity<AuthenticatedUser> entity = createHttpEntity(authenticatedUser);
        try {
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "account/" + accountId, HttpMethod.GET, entity, Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    @Override
    public Account getAccountByUserId(AuthenticatedUser authenticatedUser, long userId) {
        Account account = null;
        HttpEntity<AuthenticatedUser> entity = createHttpEntity(authenticatedUser);
        try {
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "account/" + userId, 
                    HttpMethod.GET, entity, Account.class);
            //System.out.println("test");
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
            e.printStackTrace();
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
