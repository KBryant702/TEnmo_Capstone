package com.techelevator.tenmo.exceptions;

public class InvalidUserChoiceException extends Exception{
    
    public InvalidUserChoiceException(){
        super("Current selection cannot be your own account. Please enter a new TEnmo customer.");
    }
    
}
