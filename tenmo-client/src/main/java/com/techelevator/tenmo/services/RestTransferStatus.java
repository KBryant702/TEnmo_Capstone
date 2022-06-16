package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTransferStatus implements TransferStatusService{
    
    private RestTemplate restTemplate;
    public static final String API_BASE_URL = "http://localhost:5432/tenmo";

    @Override
    public Transfer getTransferStatus(AuthenticatedUser authenticatedUser, String description) {
        Transfer transferStatus = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "/transfer_status/filter?description="    // transferstatus needs revision as it's not setup in this form
                    + description, HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer.class);
            transferStatus = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return transferStatus;
    }

    @Override
    public Transfer getTransferStatusById(AuthenticatedUser authenticatedUser, long transferStatusId) {
        Transfer transferStatus = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "/transfer_status/" + transferStatusId,  // same as stated in above method
                    HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer.class);
            transferStatus = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return transferStatus;
    }

    @Override
    public boolean updateTransferStatus(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);
        boolean success = false;

        try {
            restTemplate.put(API_BASE_URL + "/transfer/" + transfer.getTransferId(), HttpMethod.PUT, entity, Transfer.class);
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;

    }







    private HttpEntity<AuthenticatedUser> createHttpEntity(AuthenticatedUser authenticatedUser) {    //confirm this is supposed to be authenticatedUser
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(authenticatedUser, headers);
    }


}
