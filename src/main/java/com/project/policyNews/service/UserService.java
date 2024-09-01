package com.project.policyNews.service;

import com.project.policyNews.entity.User;
import com.project.policyNews.model.Auth;
import com.project.policyNews.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("사용자를 찾을 수 없습니다 : " + username));

    String password = passwordEncoder.encode(user.getPassword());
    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getUsername())
        .password(password)
        .roles(user.getRoles().toArray(new String[0]))
        .build();
  }

  // 사용자 등록 메서드
  public User registerUser(Auth.register user) {
    user.setPassword(this.passwordEncoder.encode(user.getPassword()));

    return userRepository.save(User.builder().username(user.getUsername())
        .password(user.getPassword())
        .roles(user.getRoles())
        .build());
  }

  public User loginUser(Auth.login user) {
    var userLogin = this.userRepository.findByUsername(user.getUsername())
        .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));

    if (!this.passwordEncoder.matches(user.getPassword(), userLogin.getPassword())) {
      throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    }
    return userLogin;
  }

  // 사용자 존재 여부 확인 메서드
  public boolean userExists(String username) {
    return userRepository.existsByUsername(username);
  }
}
