package com.project.policyNews.controller;

import com.project.policyNews.model.BookmarkDto;
import com.project.policyNews.service.BookmarkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

  private final BookmarkService bookmarkService;

  /*
   * [회원만] 마이페이지 - 카테고리 별로 즐겨찾기 한 기사를 조회
   */
  @GetMapping("/search")
  @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
  public ResponseEntity<?> getBookmarkListByCategory(
      @RequestParam(value = "userId") Long userId,
      @RequestParam(value = "categoryId") Long categoryId) {
    try {
      return ResponseEntity.ok(bookmarkService.getBookMarkByCategory(userId, categoryId));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /*
   * [회원만] 즐겨찾기 추가
   */
  @PostMapping("/register")
  @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
  public ResponseEntity<?> registerBookMark(@RequestBody BookmarkDto bookmarkDto) {
    try {
      return ResponseEntity.ok(bookmarkService.registerBookMark(bookmarkDto));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /*
   * [회원만] 즐겨찾기 수정(카테고리 변경, 태그 수정, 삭제)
   */
  @PostMapping("/modify")
  @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
  public ResponseEntity<?> modifyBookMark(@RequestBody BookmarkDto bookmarkDto) {
    try {
      return ResponseEntity.ok(bookmarkService.modifyBookMark(bookmarkDto));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /*
   * [회원만] 즐겨찾기 해제
   */
  @PostMapping("/delete")
  @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
  public ResponseEntity<?> deleteBookMark(@RequestBody BookmarkDto bookmarkDto) {
    try {
      return ResponseEntity.ok(bookmarkService.deleteBookMark(bookmarkDto));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /*
   * [회원만] 태그내역 변경
   */
  @PutMapping("/tag/update")
  @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
  public ResponseEntity<?> updateTags(
      @RequestParam(value = "bookmarkId") Long bookmarkId,
      @RequestBody List<String> Tags) {
    try {
      return ResponseEntity.ok(bookmarkService.updateTags(bookmarkId, Tags));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /*
   * [회원만] 태그 삭제
   */
  @DeleteMapping("/tag/delete")
  @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
  public ResponseEntity<?> deleteTags(
      @RequestParam(value = "bookmarkId") Long bookmarkId,
      @RequestParam(value = "Tag") String Tag) {
    try {
      return ResponseEntity.ok(bookmarkService.deleteTag(bookmarkId, Tag));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
