package com.project.policyNews.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import type.NewsStateEnumerated;
import type.NewsTypeEnumerated;

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
  private NewsStateEnumerated status;

  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate standardDate;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
  private LocalDateTime approveDateTime;

  @Column
  private String approverName;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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
  private NewsTypeEnumerated type;

  @Column(length = 5000)
  private String contents;

  @Column
  private String ministerCode;

  @Column
  private String articleUrl;
//
//  @CreatedDate
//  private LocalDateTime createdDateTime;
//
//  @LastModifiedDate
//  private LocalDateTime updatedDateTime;

  public void setContents(String contents) {
    if (contents != null && contents.length() > 5000) {
      this.contents = contents.substring(0, 5000);
    } else {
      this.contents = contents;
    }
  }

}
