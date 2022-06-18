package com.techelevator.tenmo.exceptions;

public class InvalidTransferIdChoice extends Exception{
    public InvalidTransferIdChoice(){
        super("Invalid transfer Id, please enter a valid transfer Id.");
    }
}
