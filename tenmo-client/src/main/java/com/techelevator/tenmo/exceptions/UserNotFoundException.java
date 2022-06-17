package com.techelevator.tenmo.exceptions;

public class UserNotFoundException extends Exception{
    
    public UserNotFoundException(){
        super("The user you have chosen does not exist. Please enter a valid user ID");
    }
    
}
