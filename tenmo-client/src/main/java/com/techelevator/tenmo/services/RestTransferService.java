package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

@Component
public class RestTransferService implements TransferService {

    private RestTemplate restTemplate = new RestTemplate();
    private final String API_BASE_URL;

    public RestTransferService(String API_BASE_URL) {
        this.API_BASE_URL = API_BASE_URL;
    }

    @Override
    public void createTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);
        try {
            restTemplate.exchange(API_BASE_URL + "transfer/", HttpMethod.POST, entity, Transfer.class);
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
//            e.printStackTrace();      // for testing only
        } catch (RestClientException e) {
            BasicLogger.log(e.getMessage());
//            e.printStackTrace();      // for testing only
        }
    }

    @Override
    public Transfer[] getTransfersByUserId(AuthenticatedUser authenticatedUser, long userId) {
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.exchange(API_BASE_URL + "transfer/tenmo_user/" + userId, HttpMethod.GET, 
                    createHttpEntity(authenticatedUser), Transfer[].class).getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
//            e.printStackTrace();      // for testing only
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
//            e.printStackTrace();      // for testing only

        }
        return transfers;
    }

    @Override
    public Transfer getTransferByTransferId(AuthenticatedUser authenticatedUser, long transferId) {
        Transfer transfer = null;
        try {
            transfer = restTemplate.exchange(API_BASE_URL + "/transfer/" + transferId, 
                    HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer.class).getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
//            e.printStackTrace();      // for testing only
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
//            e.printStackTrace();      // for testing only
        }

        return transfer;
    }

    @Override
    public Transfer[] getAllTransfers(AuthenticatedUser authenticatedUser) {
        Transfer[] transfers = new Transfer[0];
        try {
            transfers = restTemplate.exchange(API_BASE_URL + "/transfer", HttpMethod.GET, 
                    createHttpEntity(authenticatedUser), Transfer[].class).getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
//            e.printStackTrace();      // for testing only
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
//            e.printStackTrace();      // for testing only
        }

        return transfers;
    }

    @Override
    public Transfer transferDetails(AuthenticatedUser authenticatedUser, long transferId) {
        Transfer transfer = null;
        try{
            transfer = restTemplate.exchange(API_BASE_URL + "/transfer/" +transferId, HttpMethod.GET, createHttpEntity(authenticatedUser), 
                    Transfer.class).getBody();
        }catch(RestClientResponseException e){
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
//            e.printStackTrace();      // for testing only
        }catch(ResourceAccessException e){
            BasicLogger.log(e.getMessage());
//            e.printStackTrace();      // for testing only
        }
        return transfer;
    }

    private HttpEntity<AuthenticatedUser> createHttpEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(authenticatedUser, headers);
    }


}
