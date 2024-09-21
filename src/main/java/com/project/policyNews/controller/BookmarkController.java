package com.project.policyNews.controller;

import com.project.policyNews.model.BookmarkDto;
import com.project.policyNews.service.BookmarkService;
import java.time.LocalDate;
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
@RequiredArgsConstructor
@RequestMapping("/policy-news")
public class BookmarkController {

  private final BookmarkService bookmarkService;

  /*
   * [회원만] 마이페이지 - 카테고리 별로 즐겨찾기 한 기사를 조회
   */
  @GetMapping("/bookmarks")
  @PreAuthorize("hasRole('READ')")
  public ResponseEntity<?> getBookmarkListByCategory(
      @RequestParam(value = "categoryId") Long categoryId) {
    try {
      return ResponseEntity.ok(bookmarkService.getBookMarkByCategory(categoryId));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /*
   * [회원만] 즐겨찾기 추가
   */
  @PostMapping("/bookmark")
  @PreAuthorize("hasRole('WRITE')")
  public ResponseEntity<?> registerBookMark(@RequestBody BookmarkDto bookmarkDto) {
    try {
      return ResponseEntity.ok(bookmarkService.registerBookMark(bookmarkDto));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /*
   * [회원만] 즐겨찾기 수정(카테고리 변경)
   */
  @PutMapping("/bookmark")
  @PreAuthorize("hasRole('WRITE')")
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
  @DeleteMapping("/bookmark")
  @PreAuthorize("hasRole('WRITE')")
  public ResponseEntity<?> deleteBookMark(@RequestBody BookmarkDto bookmarkDto) {
    try {
      bookmarkService.deleteBookMark(bookmarkDto);

      return ResponseEntity.ok("즐겨찾기 해제되었습니다.");

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /*
   * [회원만] 태그내역 변경
   */
  @PutMapping("/bookmark/tag")
  @PreAuthorize("hasRole('WRITE')")
  public ResponseEntity<?> updateTags(
      @RequestParam(value = "bookmarkId") Long bookmarkId,
      @RequestBody List<String> tags) {
    try {
      return ResponseEntity.ok(bookmarkService.updateTags(bookmarkId, tags));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /*
   * [회원만] 태그 삭제
   */
  @DeleteMapping("/bookmark/tag")
  @PreAuthorize("hasRole('WRITE')")
  public ResponseEntity<?> deleteTags(
      @RequestParam(value = "bookmarkId") Long bookmarkId,
      @RequestParam(value = "tag") String tag) {
    try {
      return ResponseEntity.ok(bookmarkService.deleteTag(bookmarkId, tag));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /*
   * [회원만] 마이페이지 - 즐겨찾기된 기사의 게시일 기준, 인기 뉴스(hot news) size건만큼 조회
   */
  @GetMapping("/HotNews")
  @PreAuthorize("hasRole('READ')")
  public ResponseEntity<?> getHotNewsPolicy(
      @RequestParam(value = "searchDate") LocalDate searchDate,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    try {
      return ResponseEntity.ok(bookmarkService.getHotNewsPolicy(searchDate, size));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
