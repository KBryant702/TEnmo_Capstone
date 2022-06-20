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
//        String url = API_BASE_URL + "transfer/";
        System.out.println("test");
        try {
            System.out.println("i'm in");
            restTemplate.exchange(API_BASE_URL + "transfer/", HttpMethod.POST, entity, Transfer.class);
            System.out.println("template test");
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
            e.printStackTrace();
        } catch (RestClientException e) {
            BasicLogger.log(e.getMessage());
        }
    }
//    
//    @Override
//    public void updateTransfer(AuthenticatedUser authenticatedUser, Transfer transfer){
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(authenticatedUser.getToken());
//        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);
//        String url = API_BASE_URL + "/transfer/" + transfer.getTransferId();
//        
//        try{
//            restTemplate.exchange(url, HttpMethod.PUT, entity, Transfer.class);
//        }catch(RestClientResponseException | ResourceAccessException e){
//            BasicLogger.log(e.getMessage());
//        }
//    }

    @Override
    public Transfer[] getTransfersByUserAccountId(AuthenticatedUser authenticatedUser, long userAccountId) {
        Transfer[] transfers = null;
        try {
//            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/transfer/tenmo_user/" + userId, HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer[].class);
//            transfers = response.getBody();
            transfers = restTemplate.exchange(API_BASE_URL + "transfer/tenmo_user/" + userAccountId, HttpMethod.GET, 
                    createHttpEntity(authenticatedUser), Transfer[].class).getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return transfers;
    }

    @Override
    public Transfer getTransferByTransferId(AuthenticatedUser authenticatedUser, long transferId) {
        Transfer transfer = null;
        try {
//            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "/transfer/" + transferId, HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer.class);
//            transfer = response.getBody();
            transfer = restTemplate.exchange(API_BASE_URL + "/transfer/" + transferId, 
                    HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer.class).getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return transfer;
    }

    @Override
    public Transfer[] getAllTransfers(AuthenticatedUser authenticatedUser) {
        Transfer[] transfers = new Transfer[0];
        try {
//            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/transfer", HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer[].class);
//            transfers = response.getBody();
            transfers = restTemplate.exchange(API_BASE_URL + "/transfer", HttpMethod.GET, 
                    createHttpEntity(authenticatedUser), Transfer[].class).getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return transfers;
    }

//    @Override
//    public Transfer[] getPendingTransfersByUserId(AuthenticatedUser authenticatedUser) {
//        Transfer[] transfers = null;
//        try {
////            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/transfer/tenmo_user/" + authenticatedUser.getUser().getId() + "/pending",
////                    HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer[].class);
////            transfers = response.getBody();
//            transfers = restTemplate.exchange(API_BASE_URL + "/transfer/tenmo_user/" + 
//                    authenticatedUser.getUser().getId() + "/pending", HttpMethod.GET, 
//                    createHttpEntity(authenticatedUser), Transfer[].class).getBody();
//        } catch (RestClientResponseException e) {
//            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
//        } catch (ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//        }
//
//        return transfers;
//    }

    private HttpEntity<AuthenticatedUser> createHttpEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(authenticatedUser, headers);
    }


}
