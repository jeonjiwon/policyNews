package com.project.policyNews.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.policyNews.entity.User;
import com.project.policyNews.model.Auth;
import com.project.policyNews.security.JwtAuthenticationFilter;
import com.project.policyNews.security.JwtTokenProvider;
import com.project.policyNews.service.UserService;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
//@SpringBootTest
@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @Test
  public void 회원가입_성공() throws Exception {

    //given
    Auth.register user = new Auth.register();
    user.setUsername("testuser");
    user.setPassword("password123");
    user.setRoles(Arrays.asList("USER"));
    Gson gson = new Gson();
    String content = gson.toJson(user);

    //when
    ResultActions actions = mockMvc.perform(
        MockMvcRequestBuilders.
            post("/auth/register")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(content))
    );

    //then
    MvcResult mvcResult = actions.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("User registered successfully"))
        .andReturn();

    String response = mvcResult.getResponse().getContentAsString();
    Assertions.assertThat(response).isEqualTo("User registered successfully");

  }
}
