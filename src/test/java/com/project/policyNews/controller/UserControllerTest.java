package com.project.policyNews.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.policyNews.PolicyNewsApplication;
import com.project.policyNews.entity.User;
import com.project.policyNews.model.Auth;
import com.project.policyNews.security.JwtAuthenticationFilter;
import com.project.policyNews.security.JwtTokenProvider;
import com.project.policyNews.service.UserService;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.google.gson.Gson;


@SpringBootTest(classes = PolicyNewsApplication.class)
@AutoConfigureMockMvc
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserService userService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

//  @Test
//  @Transactional
//  public void 회원가입_성공() throws Exception {
//
//    //given
//    Auth.register user = new Auth.register();
//    user.setUsername("test1");
//    user.setPassword("pw1");
//    user.setRoles(Arrays.asList("READ"));
//
//    //when
//    ResultActions actions = mockMvc.perform(
//        MockMvcRequestBuilders.
//            post("/auth/register")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(user))
//    );
//
//    //then
//    MvcResult mvcResult = actions.andExpect(MockMvcResultMatchers.status().isOk())
//        .andExpect(MockMvcResultMatchers.content().string("회원가입이 성공적으로 처리되었습니다."))
//        .andReturn();
//
//    String response = mvcResult.getResponse().getContentAsString();
//    Assertions.assertThat(response).isEqualTo("회원가입이 성공적으로 처리되었습니다.");
//
//  }
}
