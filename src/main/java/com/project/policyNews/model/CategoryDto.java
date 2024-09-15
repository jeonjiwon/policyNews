package com.project.policyNews.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CategoryDto {
  private Long cateGoryId;
  private Long userId;
  private String name;
}
