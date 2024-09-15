package com.project.policyNews.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDto {
  private Long categoryId;
  private String name;
  private Long userId;
  private Long bookmarkId;
  private Long id;
  private String title;
  private String tags;
  private List<String> tagsList;

  public BookmarkDto(Long categoryId, String name, Long userId, Long bookmarkId, Long id,
      String title, String tags) {
    this.categoryId = categoryId;
    this.name = name;
    this.userId = userId;
    this.bookmarkId = bookmarkId;
    this.id = id;
    this.title = title;
    this.tags = tags;
  }
}
