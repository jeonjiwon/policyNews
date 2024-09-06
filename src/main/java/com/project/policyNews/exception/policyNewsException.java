package com.project.policyNews.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import type.ErrorCode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class policyNewsException extends RuntimeException{

  private ErrorCode errorCode;
  private String ErrorMessage;

  public policyNewsException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.ErrorMessage = errorCode.getDescription();
  }
}
