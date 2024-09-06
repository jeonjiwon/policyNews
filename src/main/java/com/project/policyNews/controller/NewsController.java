package com.project.policyNews.controller;

import com.project.policyNews.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import type.ErrorCode;


@Slf4j
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

  private final NewsService newsService;

  /*
   * [모든사용자허용] 정책뉴스 조회
   * 페이징(size고정), Redis 캐쉬 관리
   * 조회조건 :  조회일(필수), 키워드(선택-LIKE검색)
   */
  @GetMapping("/search")
  public ResponseEntity<?> searchNews(
      @RequestParam(value = "searchDate") String searchDate,
      @RequestParam(value = "keyword", required = false) String keyword,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    try {
      return ResponseEntity.ok(newsService.searchNews(searchDate, keyword, page, size));
    } catch (Exception e) {
      return ResponseEntity.ok(e.getMessage());
    }

  }
}
