package com.project.policyNews.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.policyNews.PolicyNewsApplication;
import com.project.policyNews.entity.Bookmark;
import com.project.policyNews.entity.Category;
import com.project.policyNews.entity.News;
import com.project.policyNews.entity.User;
import com.project.policyNews.repository.BookmarkRepository;
import com.project.policyNews.repository.CategoryRepository;
import com.project.policyNews.repository.NewsRepository;
import com.project.policyNews.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = PolicyNewsApplication.class)
@AutoConfigureMockMvc
class BookmarkControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BookmarkRepository bookmarkRepository;

  @MockBean
  private NewsRepository newsRepository;

  @MockBean
  private CategoryRepository categoryRepository;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {


  }
//
//  @Test
//  @WithMockUser(username = "wjseldnjs", roles = {"READ"})
//  public void 북마크s조회() throws Exception {
//
//    News news = new News();
//    news.setId(1L);
//    news.setTitle("기사명");
//    newsRepository.save(news);
//
//    User user = new User();
//    user.setUserId(1L);
//    user.setUsername("wjseldnjs");
//
//    Category category = new Category();
//    category.setCategoryId(1L);
//    category.setName("test카테고리");
//    category.setUser(user);
//    categoryRepository.save(category);
//
//    Bookmark bookmark = new Bookmark();
//    bookmark.setBookMarkId(1L);
//    bookmark.setCategory(category);
//    bookmark.setUser(user);
////    bookmark.setTags("지원|해피");
//    bookmarkRepository.save(bookmark);
//
//
//    // given
//    Long userId = 1L;
//    Long categoryId = category.getCategoryId();
//    Long bookmarkId = bookmark.getBookMarkId();
//
//    // when
//    ResultActions actions = mockMvc.perform(get("/policy-news/bookmarks")
//            .param("userId", userId.toString())
//            .param("categoryId", String.valueOf(categoryId))
//          .contentType(MediaType.APPLICATION_JSON)
//    );
//
//    // then
//    actions.andExpect(status().isOk())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//        .andExpect(jsonPath("$.length()").value(1))
//        .andExpect(jsonPath("$[0].categoryId").value(categoryId))
//        .andExpect(jsonPath("$[0].name").value("test카테고리"))
//        .andExpect(jsonPath("$[0].userId").value(userId))
//        .andExpect(jsonPath("$[0].bookmarkId").value(bookmarkId))
//        .andExpect(jsonPath("$[0].id").value(1L))
//        .andExpect(jsonPath("$[0].someField").value("expectedValue"));
//
//  }


}