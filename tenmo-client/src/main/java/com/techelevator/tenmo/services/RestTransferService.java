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
    public Transfer[] getTransfersByUserId(AuthenticatedUser authenticatedUser, long userId) {
        Transfer[] transfers = null;
        try {
//            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/transfer/tenmo_user/" + userId, HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer[].class);
//            transfers = response.getBody();
            transfers = restTemplate.exchange(API_BASE_URL + "transfer/tenmo_user/" + userId, HttpMethod.GET, 
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
    @Override
    public Transfer transferDetails(AuthenticatedUser authenticatedUser, long transferId) {
        Transfer transfer = null;
        try{
            transfer = restTemplate.exchange(API_BASE_URL + "/transfer/" +transferId, HttpMethod.GET, createHttpEntity(authenticatedUser), 
                    Transfer.class).getBody();
        }catch(RestClientResponseException e){
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
            e.printStackTrace();
        }catch(ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return transfer;
//        System.out.print("-------------------------------------------\r\n" +
//                "Please enter transfer ID to view details (0 to cancel): ");
//        Scanner scanner = new Scanner(System.in);
//        String input = scanner.nextLine();
//        if (Integer.parseInt(input) != 0) {
//            boolean foundTransferId = false;
//            for (Transfer transfer : transfers) {
//                if (Integer.parseInt(input) == transfer.getTransferId()) {
//                    Transfer temp = restTemplate.exchange(API_BASE_URL + "transfer/" + transfer.getTransferId(), HttpMethod.GET, createHttpEntity(authenticatedUser), Transfer.class).getBody();
//                    foundTransferId = true;
//                    System.out.println("--------------------------------------------\r\n" +
//                            "Transfer Details\r\n" +
//                            "--------------------------------------------\r\n" +
//                            " Id: " + temp.getTransferId() + "\r\n" +
//                            " From: " + temp.getUserFrom() + "\r\n" +
//                            " To: " + temp.getUserTo() + "\r\n" +
//                            " Type: " + temp.getTransferType().getTransferTypeById() + "\r\n" +
//                            " Status: " + temp.getTransferStatus().getTransferStatusById() + "\r\n" +
//                            " Amount: $" + temp.getAmount());
//                }
//            }
//            if (!foundTransferId) {
//                System.out.println("Not a valid transfer ID");
//            }
//        }
    }

    private HttpEntity<AuthenticatedUser> createHttpEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(authenticatedUser, headers);
    }


}
