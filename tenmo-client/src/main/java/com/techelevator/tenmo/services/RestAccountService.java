package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Balance;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;


@Component
public class RestAccountService implements AccountService {
    
    private RestTemplate restTemplate;
    public static final String API_BASE_URL = "http://localhost:8080/account"; // insert final url here
    
    @Override
    public Balance getBalance(AuthenticatedUser authenticatedUser){
        Balance balance = null;
         balance = restTemplate.getForObject(API_BASE_URL + authenticatedUser, Balance.class);
         return balance;
    }
    
}
