package com.project.policyNews.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.policyNews.entity.Bookmark;
import com.project.policyNews.entity.Category;
import com.project.policyNews.entity.News;
import com.project.policyNews.entity.User;
import com.project.policyNews.model.BookmarkDto;
import com.project.policyNews.repository.BookmarkRepository;
import com.project.policyNews.repository.CategoryRepository;
import com.project.policyNews.repository.NewsRepository;
import com.project.policyNews.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class BookmarkService {

  private final BookmarkRepository bookmarkRepository;

  private final CategoryRepository categoryRepository;

  private final UserRepository userRepository;

  private final NewsRepository newsRepository;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public List<BookmarkDto> getBookMarkByCategory(Long userId, Long categoryId) throws Exception {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 입니다. "));

    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new RuntimeException("이미 삭제된 카테고리 입니다."));

    return bookmarkRepository.findAllByCategoryIdWithDetails(category.getCategoryId());
  }

  public Bookmark registerBookMark(BookmarkDto bookmarkDto) throws Exception {
    log.info("started to registerBookMark");

    Category category = categoryRepository.findById(bookmarkDto.getCategoryId())
        .orElseThrow(() -> new RuntimeException("유효하지 않은 카테고리 입니다. "));

    User user = userRepository.findById(category.getUser().getUserId())
        .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 입니다. "));

    News news = newsRepository.findById(bookmarkDto.getId())
        .orElseThrow(() -> new RuntimeException("유효하지 않은 기사 정보 입니다. "));

    List<BookmarkDto> existList = bookmarkRepository.findByUserIdAndId(user.getUserId(),
        bookmarkDto.getId());
    if (existList != null && existList.size() > 0) {
      throw new RuntimeException("이미 즐겨찾기 한 기사입니다.");
    }

    Bookmark bookmark = new Bookmark();
    bookmark.setCategory(category);
    bookmark.setNews(news);
    bookmark.setUser(user);
    bookmark.setTags(objectMapper.writeValueAsString(bookmarkDto.getTagsList()));
    bookmarkRepository.save(bookmark);

    log.info("end to registerBookMark");

    return bookmark;
  }


  public Bookmark modifyBookMark(BookmarkDto bookmarkDto) throws Exception {
    // 즐겨찾기 된 항목의 카테고리 변경
    Bookmark bookmark = bookmarkRepository.findById(bookmarkDto.getBookmarkId())
        .orElseThrow(() -> new RuntimeException("유효하지 않은 즐겨찾기 정보 입니다. "));

    User user = userRepository.findById(bookmark.getUser().getUserId())
        .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 입니다. "));

    Category category = categoryRepository.findById(bookmarkDto.getCategoryId())
        .orElseThrow(() -> new RuntimeException("유효하지 않은 카테고리 입니다. "));


    bookmark.setCategory(category);  //카테고리 변경
    bookmark.setTags(objectMapper.writeValueAsString(bookmarkDto.getTagsList()));
    bookmarkRepository.save(bookmark);
    return bookmark;
  }

  public Bookmark deleteBookMark(BookmarkDto bookmarkDto) throws Exception {
    Bookmark bookmark = bookmarkRepository.findById(bookmarkDto.getBookmarkId())
        .orElseThrow(() -> new RuntimeException("유효하지 않은 즐겨찾기 정보 입니다. "));

    User user = userRepository.findById(bookmark.getUser().getUserId())
        .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 입니다. "));

    Category category = categoryRepository.findById(bookmark.getCategory().getCategoryId())
        .orElseThrow(() -> new RuntimeException("유효하지 않은 카테고리 입니다. "));

    bookmarkRepository.deleteById(bookmark.getBookMarkId());
    return bookmark;
  }

  public Bookmark updateTags(Long bookmarkId, List<String> Tags) throws Exception {
    Optional<Bookmark> optionalNews = bookmarkRepository.findById(bookmarkId);

    if (optionalNews.isPresent()) {
      Bookmark bookmark = optionalNews.get();
      bookmark.setTags(objectMapper.writeValueAsString(Tags));
      return bookmarkRepository.save(bookmark);
    } else {
      throw new RuntimeException("태그 내역을 찾을 수 없습니다. ");
    }
  }

  public Bookmark deleteTag(Long bookmarkId, String tag) throws Exception {
    // 태그 삭제 (특정 태그만 삭제)
    Optional<Bookmark> optionalNews = bookmarkRepository.findById(bookmarkId);

    if (optionalNews.isPresent()) {
      Bookmark bookmark = optionalNews.get();
      List<String> tags = objectMapper.readValue(bookmark.getTags(), List.class);
      tags.remove(tag);
      bookmark.setTags(objectMapper.writeValueAsString(tags));

      return bookmarkRepository.save(bookmark);

    } else {
      throw new RuntimeException("태그 내역을 찾을 수 없습니다. ");
    }
  }

}
