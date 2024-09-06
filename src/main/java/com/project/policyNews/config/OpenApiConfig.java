package com.project.policyNews.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Getter
@Setter
public class OpenApiConfig {

  @Value("${open.api.data.url}")
  private String url;

  @Value("${open.api.data.key}")
  private String key;

}
