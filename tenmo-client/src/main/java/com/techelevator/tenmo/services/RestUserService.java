package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class RestUserService implements UserService{
    
    private RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:5432/tenmo";
    
    public RestUserService(){
        this.restTemplate = new RestTemplate();
    }
    
    @Override
    public User[] getAllUsers(AuthenticatedUser authenticatedUser){
        User[] users = null;
        
        try{
            users = restTemplate.exchange(API_BASE_URL + "/user", HttpMethod.GET, createEntity(authenticatedUser),
                    User[].class).getBody();
        }catch(RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return users;
    }

    @Override
    public User getUserByUserId(AuthenticatedUser authenticatedUser, long userId) {
        User user = null;
        try {
            user = restTemplate.exchange(API_BASE_URL + "/user/" + userId, HttpMethod.GET, createEntity(authenticatedUser), User.class).getBody();
        } catch(RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }


    private HttpEntity createEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity(httpHeaders);
        return entity;
    }
    
    
}
