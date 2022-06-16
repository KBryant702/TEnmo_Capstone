package com.techelevator.tenmo.model;

import javax.validation.constraints.NotEmpty;

/**
 * DTO for storing a user's credentials.
 */
public class LoginDTO {
   @NotEmpty
   private String username;
   @NotEmpty
   private String password;

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   @Override
   public String toString() {
      return "LoginDTO{" +
              "username='" + username + '\'' +
              ", password='" + password + '\'' +
              '}';
   }
}
