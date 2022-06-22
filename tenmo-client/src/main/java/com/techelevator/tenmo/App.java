package com.techelevator.tenmo;

import com.techelevator.tenmo.exceptions.InvalidTransferIdChoice;
import com.techelevator.tenmo.exceptions.InvalidUserChoiceException;
import com.techelevator.tenmo.exceptions.UserNotFoundException;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.util.BasicLogger;
import org.openqa.selenium.remote.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private AuthenticatedUser currentUser;
    private AccountService accountService;
    private AuthenticationService authenticationService;
    private UserService userService;
    private TransferTypeService transferTypeService;
    private TransferStatusService transferStatusService;
    private TransferService transferService;
    private ConsoleService consoleService;
    private static long transferIdNumber;
    private RestTemplate restTemplate;

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
        long currentUserId = userService.getUserByUserId(currentUser, currentUser.getUser().getId()).getId();
        Transfer[] transfers = transferService.getTransfersByUserId(currentUser, currentUserId);
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID          From/To                 Amount");
        System.out.println("-------------------------------------------");

        String fromOrTo = "";
        String name = "";
        for (Transfer transfer : transfers) {
            if (currentUser.getUser().getId() == transfer.getAccountFrom()) {
                fromOrTo = "From ";
                name = transfer.getUserFrom();
            } else {
                fromOrTo = "To: ";
                name = transfer.getUserTo();
            }
            System.out.println(transfer.getTransferId() + "\t\t" + fromOrTo + name + "\t\t" + "\t\t$" + transfer.getAmount());
        }
        // begin handling of transfer details here
        int userSelection = consoleService.promptForInt("Please enter a transfer Id or press 0 to return to previous menu.");
        if (userSelection == 0) {
            return;
        }
        Transfer transfer = transferService.transferDetails(currentUser, userSelection);
        System.out.println("--------------------------------------------\r\n" +
                "Transfer Details\r\n" +
                "--------------------------------------------\r\n" +
                " Id: " + transfer.getTransferId() + "\r\n" +
                " From: " + transfer.getUserFrom() + "\r\n" +
                " To: " + transfer.getUserTo() + "\r\n" +
                " Type: " + "Send" + "\r\n" +           // currently hardcoded since only returning one type
                " Status: " + "Approved" + "\r\n" +     // currently hardcoded since only returning one status
//                            " Type: " + transfer.getTransferType().getTransferTypeById() + "\r\n" +
//                            " Status: " + transfer.getTransferStatus().getTransferStatusById() + "\r\n" +
                " Amount: $" + transfer.getAmount());
    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub (optional will return to this at a later date)
    }

    private void sendBucks() {
        // TODO Auto-generated method stub
        User[] users = userService.getAllUsers(currentUser);
        printUserOptions(currentUser, users);

        int userIdChoice = consoleService.promptForInt("Enter ID of user you are sending to or press 0 to return " +
                "to previous menu.");
        if (userIdChoice == 0) {
            return;
        }
        if (validateUserChoice(userIdChoice, users, currentUser)) {
            String amountChoice = consoleService.promptForString("Enter amount");
            createTransfer(userIdChoice, amountChoice);
        }
    }

    private void requestBucks() {
        // TODO Auto-generated method stub (optional will return to this at a later date)
    }

    // simplified method to only handle creation of transfer. omitting checks for type/status for now.
    private void createTransfer(long accountChoiceUserId, String amountToSend) {
        long accountFromId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId();
        long accountToId = accountService.getAccountByUserId(currentUser, accountChoiceUserId).getAccountId();

        BigDecimal amount = new BigDecimal(amountToSend);
        // create transfer
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(2);          // 1 = request  2 = send
        transfer.setTransferStatusId(2);        // 1 = pending  2 = approved  3 = rejected
        transfer.setAccountFrom(accountFromId);
        transfer.setAccountTo(accountToId);
        transfer.setAmount(amount);

        /*
            todo in future: setup controls for the transferType and transferStatus ids
         */

        transferService.createTransfer(currentUser, transfer);
    }
    // method to check user choice when selecting userId. will throw an exception if userId not found
    // currently printing to error log instead of printing to user. will update at later date
    private boolean validateUserChoice(long userIdInput, User[] users, AuthenticatedUser currentUser) {
        if (userIdInput != 0) {
            try {
                boolean validUserIdInput = false;
                for (User user : users) {
                    if (userIdInput == currentUser.getUser().getId()) {
                        throw new InvalidUserChoiceException();
                    }
                    if (user.getId() == userIdInput) {
                        validUserIdInput = true;
                        break;
                    }
                }
                if (!validUserIdInput) {
                    throw new UserNotFoundException();
                }
                return true;
            } catch (InvalidUserChoiceException | UserNotFoundException e) {
                BasicLogger.log(e.getMessage());
            }
        }
        return false;
    }
    // todo can refactor this to not take authenticaedUser
    private void printUserOptions(AuthenticatedUser authenticatedUser, User[] users) {
        System.out.println("-------------------------------");
        System.out.println("Users");
        System.out.println("ID          Name");
        System.out.println("-------------------------------");
        consoleService.printUsers(users);
    }
}
