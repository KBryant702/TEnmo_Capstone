package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class RestTransferService implements TransferService {

    private RestTemplate restTemplate;
    public static final String API_BASE_URL = "http://localhost:5432/tenmo"; // insert final url here
    

    @Override
    public boolean createTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);
        String url = API_BASE_URL + "/transfer/" + transfer.getTransferId();
        boolean success = false;
        
        try{
            restTemplate.exchange(url, HttpMethod.POST, entity, Transfer.class);
            success = true;
        }catch(RestClientResponseException e){
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        }catch(RestClientException e){
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    @Override
    public Transfer[] getTransfersByUserId(AuthenticatedUser authenticatedUser, long userId) {
        Transfer[] transfers = null;
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/transfer/tenmo_user/" +userId, HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer[].class);
            transfers = response.getBody();
        }catch(RestClientResponseException e){
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        }catch(ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        
        return transfers;
    }

    @Override
    public Transfer getTransferByTransferId(AuthenticatedUser authenticatedUser, long transferId) {
        Transfer transfer = null;
        try{
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "/transfer/" + transferId, HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer.class);
            transfer = response.getBody();
        }catch(RestClientResponseException e){
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        }catch(ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        
        return transfer;
    }

    @Override
    public Transfer[] getAllTransfers(AuthenticatedUser authenticatedUser) {
        Transfer[] transfers = new Transfer[0];
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/transfer", HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer[].class);
            transfers = response.getBody();
        }catch(RestClientResponseException e){
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        }catch(ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        
        return transfers;
    }

    @Override
    public Transfer[] getPendingTransferByUserId(AuthenticatedUser authenticatedUser, long userId) {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/transfer/tenmo_user/" + authenticatedUser.getUser().getId() + "/pending",
                    HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        
        return transfers;
    }

    private HttpEntity<AuthenticatedUser> createHttpEntity(AuthenticatedUser authenticatedUser) {    //confirm this is supposed to be authenticatedUser
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(authenticatedUser, headers);
    }

}
