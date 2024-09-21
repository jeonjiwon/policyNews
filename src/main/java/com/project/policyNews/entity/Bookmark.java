package com.project.policyNews.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.policyNews.converter.ListToJsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "bookmark")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Bookmark {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bookMarkId;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @ManyToOne
  @JoinColumn(name = "id")
  private News news;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Convert(converter = ListToJsonConverter.class)
//  @Column(name = "tags", columnDefinition = "json")
  private List<String> tags;

  @CreatedDate
  private LocalDateTime createdsDateTime;
  @LastModifiedDate
  private LocalDateTime updatedDateTime;

}
