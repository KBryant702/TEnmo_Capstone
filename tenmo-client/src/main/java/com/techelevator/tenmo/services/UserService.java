package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserService {
    
    User[] getAllUsers(AuthenticatedUser authenticatedUser);
    
    User getUserByUserId(AuthenticatedUser authenticatedUser, long userId);
    
}
