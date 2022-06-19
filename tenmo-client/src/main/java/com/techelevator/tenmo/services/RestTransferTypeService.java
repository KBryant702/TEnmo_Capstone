package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
//import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.services.TransferTypeService;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTransferTypeService implements TransferTypeService {

    private RestTemplate restTemplate = new RestTemplate();
    private final String API_BASE_URL;

    public RestTransferTypeService(String API_BASE_URL){
        this.API_BASE_URL = API_BASE_URL;
    }
    
    @Override
    public TransferType getTransferType(AuthenticatedUser authenticatedUser, String description) {
        TransferType transferType = null;
        HttpEntity<AuthenticatedUser> entity = createHttpEntity(authenticatedUser);
        try {
            ResponseEntity<TransferType> response = restTemplate.exchange(API_BASE_URL + "transfer_type/filter?desc="     // same as stated in above method
                    + description, HttpMethod.GET, entity, TransferType.class);
            transferType = response.getBody();
//            transferType = restTemplate.exchange(API_BASE_URL + "transfer_type/filter?description=" + 
//                    description, HttpMethod.GET, entity, TransferType.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
            e.printStackTrace();
        }
        return transferType;
    }

    @Override
    public TransferType getTransferTypeById(AuthenticatedUser authenticatedUser, long transferTypeId) {
        TransferType transferType = null;
        HttpEntity<AuthenticatedUser> entity = createHttpEntity(authenticatedUser);
        try {
            ResponseEntity<TransferType> response = restTemplate.exchange(API_BASE_URL + "transfer_type/" + transferTypeId,   // same as stated in above method
                    HttpMethod.GET, entity, TransferType.class);
            transferType = response.getBody();
//            transferType = restTemplate.exchange(API_BASE_URL + "transfer_type/" + transferTypeId, HttpMethod.GET, 
//                    createHttpEntity(authenticatedUser), TransferType.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
            e.printStackTrace();
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
