package com.project.policyNews.controller;

import com.project.policyNews.entity.User;
import com.project.policyNews.model.Auth;
import com.project.policyNews.security.JwtTokenProvider;
import com.project.policyNews.service.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  private final JwtTokenProvider jwtTokenProvider;

  // 회원가입
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Auth.register user) {

    if (userService.userExists(user.getUsername())) {
      log.warn("이미 회원가입된 ID 입니다.");
      return ResponseEntity.badRequest().body("이미 회원가입된 ID 입니다.");
    }
    if (user.getRoles() == null || user.getRoles().isEmpty()) {
      user.getRoles().add("USER");
    }
    User newUser = userService.registerUser(user);

    log.info("회원가입이 성공적으로 처리되었습니다. [{}]", user.getUsername());
    return ResponseEntity.ok("회원가입이 성공적으로 처리되었습니다.");
  }

  // 로그인 API
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Auth.login user) {
    try {
      var member = this.userService.loginUser(user);
      var token = this.jwtTokenProvider.generateToken(member.getUsername(), member.getRoles());

      return ResponseEntity.ok(Map.of("token", token));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("아이디 또는 비밀번호가 일치하지 않습니다.");
    }
  }

}
