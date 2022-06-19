package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTransferStatusService implements TransferStatusService {

    private RestTemplate restTemplate;
    private final String API_BASE_URL;

    public RestTransferStatusService(String API_BASE_URL) {
        this.restTemplate = new RestTemplate();
        this.API_BASE_URL = API_BASE_URL;
    }

    @Override
    public TransferStatus[] getPendingTransfersByUserId(AuthenticatedUser authenticatedUser){
        TransferStatus[] transfers = null;
        
        try{
            transfers = restTemplate.exchange(API_BASE_URL + "/transfer/tenmo_user/" + 
                    authenticatedUser.getUser().getId() + "/pending", HttpMethod.GET, 
                    createHttpEntity(authenticatedUser), TransferStatus[].class).getBody();
        }catch(RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }
    
    @Override
    public TransferStatus getTransferStatus(AuthenticatedUser authenticatedUser, String description) {
        TransferStatus transferStatus = null;
        HttpEntity<AuthenticatedUser> entity = createHttpEntity(authenticatedUser);
        try {
            ResponseEntity<TransferStatus> response = restTemplate.exchange(API_BASE_URL + "transfer_status/filter?desc="    // transferstatus needs revision as it's not setup in this form
                    + description, HttpMethod.GET, entity, TransferStatus.class);
            transferStatus = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return transferStatus;
    }

    @Override
    public TransferStatus getTransferStatusById(AuthenticatedUser authenticatedUser, long transferStatusId) {
        TransferStatus transferStatus = null;
        try {
            ResponseEntity<TransferStatus> response = restTemplate.exchange(API_BASE_URL + "transfer_status/" + transferStatusId,  // same as stated in above method
                    HttpMethod.GET, createHttpEntity(authenticatedUser), TransferStatus.class);
            transferStatus = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return transferStatus;
    }

    @Override
    public void updateTransferStatus(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);

        try {
            restTemplate.put(API_BASE_URL + "/transfer/" + transfer.getTransferId(), HttpMethod.PUT, entity, Transfer.class);
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

    }


    private HttpEntity<AuthenticatedUser> createHttpEntity(AuthenticatedUser authenticatedUser) {    //confirm this is supposed to be authenticatedUser
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(authenticatedUser, headers);
    }


}
