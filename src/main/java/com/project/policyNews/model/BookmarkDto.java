package com.project.policyNews.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.policyNews.converter.ListToJsonConverter;
import jakarta.persistence.Convert;
import java.util.Arrays;
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

  private Long newsId;

  private String title;

//  @JsonProperty("tags")
  @Convert(converter = ListToJsonConverter.class)
  private List<String> tags;

}