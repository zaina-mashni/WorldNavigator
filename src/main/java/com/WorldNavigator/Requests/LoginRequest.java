package com.WorldNavigator.Requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {

  private String username;
  private String password;

  public LoginRequest(
      @JsonProperty(value = "username", required = true) String username,
      @JsonProperty(value = "password", required = true) String password) {
    this.username = username;
    this.password = password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public String getUsername() {
    return username;
  }
}
