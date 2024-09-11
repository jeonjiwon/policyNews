package com.project.policyNews.service;


import ch.qos.logback.core.util.StringUtil;
import com.project.policyNews.config.OpenApiConfig;
import com.project.policyNews.entity.News;
import com.project.policyNews.repository.NewsRepository;
import java.io.StringReader;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import type.NewsStateEnumerated;
import type.NewsTypeEnumerated;

@Slf4j
@Service
@Transactional
//@AllArgsConstructor
public class NewsOpenApiService {

  private final OpenApiConfig apiDataConfig;

  private final NewsRepository newsRepository;

  private final WebClient webClient;


  public NewsOpenApiService(OpenApiConfig apiDataConfig, NewsRepository newsRepository,
      WebClient.Builder webClientBuilder) {
    this.apiDataConfig = apiDataConfig;
    this.newsRepository = newsRepository;
    this.webClient = webClientBuilder.build();
  }

  public void newsPolicyApiCall(LocalDate searchDate) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

      URI uri = UriComponentsBuilder.fromUriString(apiDataConfig.getUrl())
          .queryParam("serviceKey", apiDataConfig.getKey())
          .queryParam("startDate", searchDate.format(formatter))
          .queryParam("endDate", searchDate.format(formatter))
          .build()
          .toUri();

      String responseBody = webClient
          .get()
          .uri(uri)
          .retrieve()
          .bodyToMono(String.class)
          .block();

      // 응답 코드 로그
      log.info("Response code: " + responseBody);
      parseAndStoreData(searchDate, responseBody);

    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }


  public void parseAndStoreData(LocalDate standardDate, String Content) {
    List<News> result = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    try {
      if (Content == null || Content.isEmpty()) {
        log.info("{} 일 -> 처리할 데이터가 없습니다. ", standardDate);
      }

      if (Content.startsWith("<?xml")) {
        Content = Content.substring(Content.indexOf("?>") + 2).trim();
      }

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(new InputSource(new StringReader(Content)));
      document.getDocumentElement().normalize();

      Element root = document.getDocumentElement();

      //  Header
      Element header = (Element) root.getElementsByTagName("header").item(0);
      String resultCode = getElementValue(header, "resultCode");
      String resultMsg = getElementValue(header, "resultMsg");

      // body
      NodeList newsItemList = root.getElementsByTagName("NewsItem");
      if ("0".equals(resultCode)) {
        log.info("Success: " + resultMsg);
        if (newsItemList.getLength() > 0) {
          for (int i = 0; i < newsItemList.getLength(); i++) {

            Element newsElement = (Element) newsItemList.item(i);
            Long newsId = Long.parseLong(getElementValue(newsElement, "NewsItemId"));
            Optional<News> existingNewsItem = newsRepository.findById(newsId);

            if (existingNewsItem.isPresent()) {
              News newsItem = existingNewsItem.get();
              newsItem.setStandardDate(standardDate);
              newsItem.setId(newsId);
              newsItem.setStatus(
                  "I".equals(getElementValue(newsElement, "ContentsStatus")) ? NewsStateEnumerated.I
                      : NewsStateEnumerated.U);
              newsItem.setApproveDateTime(
                  StringUtil.isNullOrEmpty(getElementValue(newsElement, "ApproveDate")) ? null :
                      LocalDateTime.parse(getElementValue(newsElement, "ApproveDate"), formatter));
              newsItem.setApproverName(
                  StringUtil.isNullOrEmpty(getElementValue(newsElement, "ApproverName")) ? null :
                      getElementValue(newsElement, "ApproverName"));
              newsItem.setValidDateTime(
                  StringUtil.isNullOrEmpty(getElementValue(newsElement, "EmbargoDate")) ? null :
                      LocalDateTime.parse(getElementValue(newsElement, "EmbargoDate"), formatter));
              newsItem.setGroupCode(
                  getElementValue(newsElement, "GroupingCode"));
              newsItem.setTitle(getElementValue(newsElement, "Title"));
              newsItem.setSubTitle1(
                  getElementValue(newsElement, "SubTitle1"));
              newsItem.setSubTitle2(getElementValue(newsElement, "SubTitle2"));
              newsItem.setSubTitle3(getElementValue(newsElement, "SubTitle3"));
              newsItem.setType(
                  "H".equals(getElementValue(newsElement, "ContentsType")) ? NewsTypeEnumerated.H
                      : NewsTypeEnumerated.T);
              newsItem.setContents(getElementValue(newsElement, "DataContents"));
              newsItem.setMinisterCode(getElementValue(newsElement, "MinisterCode"));
              newsItem.setArticleUrl(getElementValue(newsElement, "OriginalUrl"));

              newsRepository.save(newsItem); // 변경내역 수정
            } else {
              News newsItem = new News();
              newsItem.setStandardDate(standardDate);
              newsItem.setId(newsId);
              newsItem.setStatus(
                  "I".equals(getElementValue(newsElement, "ContentsStatus")) ? NewsStateEnumerated.I
                      : NewsStateEnumerated.U);
              newsItem.setApproveDateTime(
                  StringUtil.isNullOrEmpty(getElementValue(newsElement, "ApproveDate")) ? null :
                      LocalDateTime.parse(getElementValue(newsElement, "ApproveDate"), formatter));
              newsItem.setApproverName(
                  StringUtil.isNullOrEmpty(getElementValue(newsElement, "ApproverName")) ? null :
                      getElementValue(newsElement, "ApproverName"));
              newsItem.setValidDateTime(
                  StringUtil.isNullOrEmpty(getElementValue(newsElement, "EmbargoDate")) ? null :
                      LocalDateTime.parse(getElementValue(newsElement, "EmbargoDate"), formatter));
              newsItem.setGroupCode(
                  getElementValue(newsElement, "GroupingCode"));
              newsItem.setTitle(getElementValue(newsElement, "Title"));
              newsItem.setSubTitle1(
                  getElementValue(newsElement, "SubTitle1"));
              newsItem.setSubTitle2(getElementValue(newsElement, "SubTitle2"));
              newsItem.setSubTitle3(getElementValue(newsElement, "SubTitle3"));
              newsItem.setType(
                  "H".equals(getElementValue(newsElement, "ContentsType")) ? NewsTypeEnumerated.H
                      : NewsTypeEnumerated.T);
              newsItem.setContents(getElementValue(newsElement, "DataContents"));
              newsItem.setMinisterCode(getElementValue(newsElement, "MinisterCode"));
              newsItem.setArticleUrl(getElementValue(newsElement, "OriginalUrl"));

              newsRepository.save(newsItem); // 신규저장
            }


          }
          log.info("공공데이터API에서 데이터 {} 건을 저장하였습니다.", newsItemList.getLength());
        } else {
          log.info("공공데이터API에서 불러올 데이터가 없습니다.");
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private static String getElementValue(Element parent, String tagName) {
    NodeList nodeList = parent.getElementsByTagName(tagName);
    if (nodeList != null && nodeList.getLength() > 0) {
      return nodeList.item(0).getTextContent();
    }
    return null;
  }

}