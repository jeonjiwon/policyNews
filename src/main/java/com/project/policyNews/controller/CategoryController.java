package com.project.policyNews.controller;

import com.project.policyNews.model.CategoryDto;
import com.project.policyNews.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  /*
   * [회원만] 마이페이지 - 카테고리 조회
   * (뉴스 즐겨찾기 시 카테고리 지정하여 저장하기 위함)
   */
  @GetMapping("/search")
  @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
  public ResponseEntity<?> getCategory(
      @RequestParam(value = "userId") Long userId,
      @RequestParam(value = "keyword", required = false) String keyword) {
    try {
      return ResponseEntity.ok(categoryService.getCategory(userId, keyword));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /*
   * [회원만] 마이페이지 - 카테고리 신규 등록
   */
  @PostMapping("/create")
  @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
  public ResponseEntity<?> registerCategory(@RequestBody CategoryDto category) {
    try {
      return ResponseEntity.ok(categoryService.registerCategory(category));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /*
   * [회원만] 마이페이지 - 카테고리 내용 변경
   */
  @PostMapping("/modify")
  @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
  public ResponseEntity<?> modifyCategory(@RequestBody CategoryDto category) {
    try {
      return ResponseEntity.ok(categoryService.modifyCategory(category));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /*
   * [회원만] 마이페이지 - 카테고리 삭제
   */
  @PostMapping("/delete")
  @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
  public ResponseEntity<?> deleteCategory(@RequestBody CategoryDto category) {
    try {
      return ResponseEntity.ok(categoryService.deleteCategory(category));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
