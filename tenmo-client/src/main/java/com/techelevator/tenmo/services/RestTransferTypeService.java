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
public class RestTransferTypeService implements TransferTypeService {

    private RestTemplate restTemplate;
    public static final String API_BASE_URL = "http://localhost:5432/tenmo"; // insert final url here

    @Override
    public Transfer getTransferType(AuthenticatedUser authenticatedUser, String description) {
        Transfer transferType = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "/transfertype/filter?description="    // same as stated in above method
                    + description, HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer.class);
            transferType = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return transferType;
    }

    @Override
    public Transfer getTransferTypeById(AuthenticatedUser authenticatedUser, long transferTypeId) {
        Transfer transferType = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "transfertype/" + transferTypeId,   // same as stated in above method
                    HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer.class);
            transferType = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return transferType;
    }


    private HttpEntity<AuthenticatedUser> createHttpEntity(AuthenticatedUser authenticatedUser) {    //confirm this is supposed to be authenticatedUser
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(authenticatedUser, headers);
    }

    
}
