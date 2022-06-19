package com.techelevator.tenmo;

import com.techelevator.tenmo.exceptions.InvalidTransferIdChoice;
import com.techelevator.tenmo.exceptions.InvalidUserChoiceException;
import com.techelevator.tenmo.exceptions.UserNotFoundException;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.util.BasicLogger;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

//    private final ConsoleService consoleService = new ConsoleService();
//    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private AccountService accountService;
    private AuthenticationService authenticationService;
    private UserService userService;
    private TransferTypeService transferTypeService;
    private TransferStatusService transferStatusService;
    private TransferService transferService;
    private ConsoleService consoleService;
    private static long transferIdNumber;

    public static void main(String[] args) {
        App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
        app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        this.consoleService = console;
        this.accountService = new RestAccountService(API_BASE_URL);
        this.userService = new RestUserService();
        this.transferTypeService = new RestTransferTypeService(API_BASE_URL);
        this.transferStatusService = new RestTransferStatusService(API_BASE_URL);
        this.transferService = new RestTransferService(API_BASE_URL);
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
        Balance balance = accountService.getBalance(currentUser);
        System.out.println("Your current account balance is: $" + balance.getBalance());
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
        Transfer[] transfers = transferService.getTransfersByUserId(currentUser, currentUser.getUser().getId());
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID          From/To                 Amount");
        System.out.println("-------------------------------------------");
        long userSelectionTransferId = consoleService.promptForInt("Please enter transfer ID to view or press 0 to " +
                "return to previous menu.");
        if(userSelectionTransferId == 0){
            consoleService.printMainMenu();
        }
        for(Transfer transfer: transfers) {
            printTransferReceipt(currentUser, transfer);
        }
    }

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
        Transfer[] transfers = transferService.getPendingTransfersByUserId(currentUser);
        System.out.println("-------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID          To          Amount");
        System.out.println("-------------------------------");

        for(Transfer transfer: transfers) {
            printTransferReceipt(currentUser, transfer);
        }
        // TODO ask to view details
        int transferIdChoice = consoleService.promptForInt("Please enter transfer ID to approve/reject or " +
                "press 0 to cancel and return to main menu.)");
        if(transferIdChoice == 0){
            consoleService.printMainMenu();
        }
        Transfer transferChoice = validateTransferIdChoice(transferIdChoice, transfers, currentUser);
        if(transferChoice != null) {
            approveOrReject(transferChoice, currentUser);
        }
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        User[] users = userService.getAllUsers(currentUser);
        printUserOptions(currentUser, users);

        int userIdChoice = consoleService.promptForInt("Enter ID of user you are sending to or press 0 to return " +
                "to previous menu.");
        if(userIdChoice == 0){
            consoleService.printMainMenu();
        }
        if (validateUserChoice(userIdChoice, users, currentUser)) {
            String amountChoice = consoleService.promptForString("Enter amount");
            createTransfer(userIdChoice, amountChoice, "Send", "Approved");
        }
    }

    private void requestBucks() {
        // TODO Auto-generated method stub
        User[] users = userService.getAllUsers(currentUser);
        printUserOptions(currentUser, users);
        int userIdChoice = consoleService.promptForInt("Enter ID of user you are requesting money from or press 0 " +
                "to return to previous menu.");
        if(userIdChoice == 0){
            consoleService.printMainMenu();
        }
        if(validateUserChoice(userIdChoice, users, currentUser)) {
            String amountChoice = consoleService.promptForString("Enter amount");
            createTransfer(userIdChoice, amountChoice, "Request", "Pending");
        }
    }
    
    private Transfer createTransfer (long accountChoiceUserId, String amountToSend, String transferType, String status){
        long transferTypeId = transferTypeService.getTransferType(currentUser, transferType).getTransferTypeById();
        long transferStatusId = transferStatusService.getTransferStatus(currentUser, status).getTransferStatusById();
        long accountToId;
        long accountFromId;

        if(transferType.equals("Send")) {
            accountToId = accountService.getAccountByUserId(currentUser, accountChoiceUserId).getAccountId();
            accountFromId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId();
        } else {
            accountToId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId();
            accountFromId = accountService.getAccountByUserId(currentUser, accountChoiceUserId).getAccountId();
        }

        BigDecimal amount = new BigDecimal(amountToSend);
        Transfer transfer = new Transfer();
        transfer.setAccountFrom(accountFromId);
        transfer.setAccountTo(accountToId);
        transfer.setAmount(amount);
        transfer.setTransferStatusId(transferStatusId);
        transfer.setTransferTypeId(transferTypeId);
        transfer.setTransferId(transferIdNumber);

        transferService.createTransfer(currentUser, transfer);
        App.incrementTransferIdNumber();
        return transfer;
    }

    public static void incrementTransferIdNumber(){
        transferIdNumber++;
    }

    private boolean validateUserChoice(long userIdInput, User[] users, AuthenticatedUser currentUser){
        if(userIdInput != 0){
            try{
                boolean validUserIdInput = false;
                for(User user : users){
                    if(userIdInput == currentUser.getUser().getId()){
                        throw new InvalidUserChoiceException();
                    }
                    if(user.getId() == userIdInput){
                        validUserIdInput = true;
                        break;
                    }
                }
                if(!validUserIdInput){
                    throw new UserNotFoundException();
                }
                return true;
            }catch(InvalidUserChoiceException | UserNotFoundException e){
                BasicLogger.log(e.getMessage());
            }
        }
        return false;
    }
    

    private void approveOrReject(Transfer pendingTransfer, AuthenticatedUser authenticatedUser){
        consoleService.printApproveOrRejectOptions();
        int choice = consoleService.promptForInt("Please choose to Accept, Reject or Cancel");
        long transferStatusId;
        
        if(choice > 2 || choice < 0){
            System.out.println("Invalid choice");
        }
        
        switch(choice){
            case 0: consoleService.printMainMenu();
                break;
            case 1: transferStatusId = transferStatusService.getTransferStatus(currentUser, "Approved").getTransferStatusById();
                pendingTransfer.setTransferStatusId(transferStatusId);
                break;
            case 2: transferStatusId = transferStatusService.getTransferStatus(currentUser, "Rejected").getTransferStatusById();
                pendingTransfer.setTransferStatusId(transferStatusId);
                break;
        }
        transferService.updateTransfer(currentUser, pendingTransfer);
    }

    private Transfer validateTransferIdChoice(int transferIdChoice, Transfer[] transfers, AuthenticatedUser currentUser) {
        Transfer transferChoice = null;
        if(transferIdChoice != 0) {
            try {
                boolean validTransferIdChoice = false;
                for (Transfer transfer : transfers) {
                    if (transfer.getTransferId() == transferIdChoice) {
                        validTransferIdChoice = true;
                        transferChoice = transfer;
                        break;
                    }
                }
                if (!validTransferIdChoice) {
                    throw new InvalidTransferIdChoice();
                }
            } catch (InvalidTransferIdChoice e) {
                System.out.println(e.getMessage());
            }
        }
        return transferChoice;
    }

    private void printUserOptions(AuthenticatedUser authenticatedUser, User[] users) {

        System.out.println("-------------------------------");
        System.out.println("Users");
        System.out.println("ID          Name");
        System.out.println("-------------------------------");
        consoleService.printUsers(users);
    }
    

    private void printTransferReceipt(AuthenticatedUser authenticatedUser, Transfer transfer) {
        String fromOrTo = "";
        long accountFrom = transfer.getAccountFrom();
        long accountTo = transfer.getAccountTo();
        if (accountService.getAccountById(currentUser, accountTo).getUserId() == authenticatedUser.getUser().getId()) {
            long accountFromUserId = accountService.getAccountById(currentUser, accountFrom).getUserId();
            String userFromName = userService.getUserByUserId(currentUser, accountFromUserId).getUsername();
            fromOrTo = "From: " + userFromName;
        } else {
            long accountToUserId = accountService.getAccountById(currentUser, accountTo).getUserId();
            String userToName = userService.getUserByUserId(currentUser, accountToUserId).getUsername();
            fromOrTo = "To: " + userToName;
        }

        consoleService.printTransfers(transfer.getTransferId(), fromOrTo, transfer.getAmount());
    }
    

}
