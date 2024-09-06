package com.project.policyNews.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import type.NewsType;
import type.NewsState;

@Entity
@Table(name = "news")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class News {

  @Id
  private Long id;

  @Column(nullable = false)
  private NewsState status;

  @Column(nullable = false)
  private String standardDate;

  @Column
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
  private LocalDateTime approveDateTime;

  @Column
  private String approverName;

  @Column
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
  private LocalDateTime validDateTime;

  @Column
  private String groupCode;

  @Column(nullable = false)
  private String title;

  @Column
  private String subTitle1;

  @Column
  private String subTitle2;

  @Column
  private String subTitle3;

  @Column
  private NewsType type;

  @Column(length = 5000)
  private String contents;

  @Column
  private String ministerCode;

  @Column
  private String articleUrl;

  @CreatedDate
  private LocalDateTime createdDateTime;

  @LastModifiedDate
  private LocalDateTime updatedDateTime;

  public void setContents(String contents) {
    if (contents != null && contents.length() > 5000) {
      this.contents = contents.substring(0, 5000);
    } else {
      this.contents = contents;
    }
  }

}
