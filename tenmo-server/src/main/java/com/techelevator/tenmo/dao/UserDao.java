package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDao  {

    List<User> findAll();

    User findByUsername(String username);

    int findIdByUsername(String username);
    
    User getUserByUserId(long userId);

    boolean create(String username, String password);
}
