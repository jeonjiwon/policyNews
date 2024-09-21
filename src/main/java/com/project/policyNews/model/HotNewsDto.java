package com.project.policyNews.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotNewsDto {

  private Long bookmarkCount;

  private Long id;

  private String title;

  private String articleUrl;

//  private String tags;

}
