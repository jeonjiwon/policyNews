package com.project.policyNews.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.policyNews.config.CustomPageImpl;
import com.project.policyNews.entity.News;
import com.project.policyNews.repository.NewsRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
//@RequiredArgsConstructor
public class NewsService {

  private final NewsOpenApiService newsOpenApiService;

  private final NewsRepository newsRepository;

  private final RedisTemplate<String, Object> redisTemplate;

  private static final String REDIS_KEY_PREFIX = "news::";

  private final ObjectMapper objectMapper = new ObjectMapper();

  public NewsService(NewsOpenApiService newsOpenApiService, NewsRepository newsRepository,
      RedisTemplate<String, Object> redisTemplate) {
    this.newsOpenApiService = newsOpenApiService;
    this.newsRepository = newsRepository;
    this.redisTemplate = redisTemplate;
  }

  public Page<News> searchNews(LocalDate searchDate, String keyword, int page, int size) {
    Page<News> resultPage;
    try {
      // 캐쉬에서 데이터 조회(key : searchDate + keyword + page + size)
      String redisKey =
          REDIS_KEY_PREFIX + searchDate + "::keyword:" + keyword + "::page:" + page + "::size:"
              + size;
      Object cachedData = redisTemplate.opsForValue().get(redisKey);

      // 캐시에 데이터가 없을때
      if (cachedData == null) {
        // db에 해당일자 데이터가 있는지 없으면 openAPI호출
        // 보완할 점 : openapi 데이터가 변경될 수도 있으므로 spring batch로 주기적으로 업데이트 처리하면 좋을 것 같음
        if (!newsRepository.existsByStandardDate(searchDate)) {
          newsOpenApiService.newsPolicyApiCall(searchDate);
        }

        // db에 해당일자 데이터가 있으면 데이터 불러와서 redis에 저장
        Pageable pageable = PageRequest.of(page, size);
        resultPage = (Page<News>) newsRepository.findAllByStandardDateAndKeyword(searchDate,
            keyword, pageable);
        redisTemplate.opsForValue().set(redisKey, resultPage.getContent());
      } else {
        // 캐시에 데이터가 있으면 타입변환해서 리턴
        String jsonString = objectMapper.writeValueAsString(cachedData);
        List<News> newsList = objectMapper.readValue(jsonString, new TypeReference<List<News>>() {});
        Pageable pageable = PageRequest.of(page, size);
        resultPage = new CustomPageImpl<>(newsList, pageable, newsList.size());
      }

    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    return resultPage;
  }

}
