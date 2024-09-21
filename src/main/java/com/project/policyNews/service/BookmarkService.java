package com.project.policyNews.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.policyNews.entity.Bookmark;
import com.project.policyNews.entity.Category;
import com.project.policyNews.entity.News;
import com.project.policyNews.entity.User;
import com.project.policyNews.model.BookmarkDto;
import com.project.policyNews.model.HotNewsDto;
import com.project.policyNews.repository.BookmarkRepository;
import com.project.policyNews.repository.CategoryRepository;
import com.project.policyNews.repository.NewsRepository;
import com.project.policyNews.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

  public List<BookmarkDto> getBookMarkByCategory(Long categoryId) throws Exception {

    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

    boolean existYn = userRepository.existsById(category.getUser().getUserId());
    if (!existYn) {
      throw new RuntimeException("유효하지 않은 사용자 입니다. ");
    }

    return bookmarkRepository.findAllByCategoryId(categoryId)
        .stream().map(b -> {
          return new BookmarkDto(b.getCategory().getCategoryId(),
              b.getCategory().getName(),
              b.getUser().getUserId(),
              b.getBookMarkId(),
              b.getNews().getId(),
              b.getNews().getTitle(),
              b.getTags());
        }).collect(Collectors.toList());
  }

  public Bookmark registerBookMark(BookmarkDto bookmarkDto) throws Exception {
    log.info("started to registerBookMark");
    Category category = categoryRepository.findById(bookmarkDto.getCategoryId())
        .orElseThrow(() -> new RuntimeException("등록되지 않은 카테고리 입니다. 카테고리 등록 후 즐겨찾기 가능합니다."));

    User user = userRepository.findById(category.getUser().getUserId())
        .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 입니다. "));

    News news = newsRepository.findById(bookmarkDto.getNewsId())
        .orElseThrow(() -> new RuntimeException("유효하지 않은 기사 정보입니다. "));

    boolean existCheck = bookmarkRepository.existsByUserIdAndNewsId(category.getUser().getUserId(),
        bookmarkDto.getNewsId());
    if (existCheck) {
      throw new RuntimeException("이미 즐겨찾기 한 기사입니다.");
    }

    Bookmark bookmark = new Bookmark();
    bookmark.setCategory(category);
    bookmark.setNews(news);
    bookmark.setUser(user);
    bookmark.setTags(bookmarkDto.getTags());

    bookmarkRepository.save(bookmark);

    log.info("end to registerBookMark");

    return bookmark;
  }


  public Bookmark modifyBookMark(BookmarkDto bookmarkDto) throws Exception {
    // 즐겨찾기 된 항목의 카테고리 변경
    Bookmark bookmark = bookmarkRepository.findById(bookmarkDto.getBookmarkId())
        .orElseThrow(() -> new RuntimeException("유효하지 않은 즐겨찾기 정보 입니다. "));

    boolean existYn = userRepository.existsById(bookmark.getUser().getUserId());
    if (!existYn) {
      throw new RuntimeException("유효하지 않은 사용자 입니다. ");
    }

    Category category = categoryRepository.findById(bookmarkDto.getCategoryId())
        .orElseThrow(() -> new RuntimeException("변경하려는 카테고리가 유효하지 않습니다. "));

    bookmark.setCategory(category);  //카테고리 변경
    bookmarkRepository.save(bookmark);
    return bookmark;
  }

  public void deleteBookMark(BookmarkDto bookmarkDto) throws Exception {
    boolean existYn = userRepository.existsById(bookmarkDto.getUserId());
    if (!existYn) {
      throw new RuntimeException("유효하지 않은 사용자 입니다. ");
    }
    existYn = bookmarkRepository.existsById(bookmarkDto.getBookmarkId());
    if (!existYn) {
      throw new RuntimeException("즐겨찾기 내역을 찾을 수 없습니다. ");
    }
    bookmarkRepository.deleteById(bookmarkDto.getBookmarkId());
  }

  public Bookmark updateTags(Long bookmarkId, List<String> tags) throws Exception {
    Optional<Bookmark> optionalNews = bookmarkRepository.findById(bookmarkId);
    if (optionalNews.isPresent()) {
      Bookmark bookmark = optionalNews.get();
      bookmark.setTags(tags);
      return bookmarkRepository.save(bookmark);
    } else {
      throw new RuntimeException("북마크 내역을 찾을 수 없습니다. ");
    }
  }

  public Bookmark deleteTag(Long bookmarkId, String tag) throws Exception {
    // 태그 삭제 (특정 태그만 삭제)
    Optional<Bookmark> optionalNews = bookmarkRepository.findById(bookmarkId);
    if (optionalNews.isPresent()) {
      Bookmark bookmark = optionalNews.get();
      List<String> tags = bookmark.getTags();
      tags.remove(tag);
      bookmark.setTags(tags);
      return bookmarkRepository.save(bookmark);

    } else {
      throw new RuntimeException("북마크 내역을 찾을 수 없습니다. ");
    }
  }


  public List<HotNewsDto> getHotNewsPolicy(LocalDate searchDate, int size) throws Exception {
    Pageable pageable = PageRequest.of(0, size); // 페이지 0, 크기 10
    return bookmarkRepository.findHotNewsWithTagsAndSearchDate(searchDate, pageable);
  }

}
