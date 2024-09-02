package com.project.policyNews.model;

import com.project.policyNews.entity.User;
import java.util.List;
import lombok.Data;

public class Auth {

  @Data
  public static class login {

    private String username;
    private String password;
  }

  @Data
  public static class register {

    private String username;
    private String password;
    private List<String> roles;

    public User toEntity() {
      return User.builder()
          .username(this.username)
          .password(this.password)
          .roles(this.roles)
          .build();
    }
  }
}
