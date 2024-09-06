package com.project.policyNews.service;


import ch.qos.logback.core.util.StringUtil;
import com.project.policyNews.config.OpenApiConfig;
import com.project.policyNews.entity.News;
import com.project.policyNews.repository.NewsRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import type.NewsState;
import type.NewsType;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NewsOpenApiService {

  private final OpenApiConfig apiDataConfig;

  private final NewsRepository newsRepository;

  public void newsPolicyApiCall(String searchDate) {
    StringBuilder urlBuilder = null;
    HttpURLConnection conn = null;
    StringBuilder sb = new StringBuilder();

    try {
      urlBuilder = new StringBuilder(apiDataConfig.getUrl());
      urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "="
          + apiDataConfig.getKey()); /*Service Key*/
      urlBuilder.append(
          "&" + URLEncoder.encode("startDate", "UTF-8") + "=" + URLEncoder.encode(searchDate,
              "UTF-8")); /*검색시작일자*/
      urlBuilder.append(
          "&" + URLEncoder.encode("endDate", "UTF-8") + "=" + URLEncoder.encode(searchDate,
              "UTF-8")); /*검색종료일자*/

      URL url = new URL(urlBuilder.toString());
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Content-type", "application/json");

      System.out.println("Response code: " + conn.getResponseCode());

      BufferedReader rd = null;
      if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
          sb.append(line);
        }
        System.out.println(sb.toString());

        // 성공 시에만 데이터 파싱
        parseAndStoreData(searchDate, sb.toString());

      } else {
        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
      }

      if (rd != null) {
        rd.close();
      }
      if (conn != null) {
        conn.disconnect();
      }

    } catch (Exception e) {
      if (conn != null) {
        conn.disconnect();
      }
      throw new RuntimeException(e.getMessage());
    }
  }


  public void parseAndStoreData(String standardDate, String Content) {
    List<News> result = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    try {
      if (Content == null || Content.isEmpty()) {
        System.out.println("Content is null or empty");
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
        System.out.println("Success: " + resultMsg);
        if(newsItemList.getLength() > 0) {
          for (int i = 0; i < newsItemList.getLength(); i++) {

            Element newsElement = (Element) newsItemList.item(i);
            Long newsId = Long.parseLong(getElementValue(newsElement, "NewsItemId"));
            Optional<News> existingNewsItem = newsRepository.findById(newsId);

            if (existingNewsItem.isPresent()) {
              News newsItem = existingNewsItem.get();
              newsItem.setStandardDate(standardDate);
              newsItem.setId(newsId);
              newsItem.setStatus(
                  "I".equals(getElementValue(newsElement, "ContentsStatus")) ? NewsState.I
                      : NewsState.U);
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
                  "H".equals(getElementValue(newsElement, "ContentsType")) ? NewsType.H : NewsType.T);
              newsItem.setContents(getElementValue(newsElement, "DataContents"));
              newsItem.setMinisterCode(getElementValue(newsElement, "MinisterCode"));
              newsItem.setArticleUrl(getElementValue(newsElement, "OriginalUrl"));

              newsRepository.save(newsItem); // 변경내역 수정
            } else {
              News newsItem = new News();
              newsItem.setStandardDate(standardDate);
              newsItem.setId(newsId);
              newsItem.setStatus(
                  "I".equals(getElementValue(newsElement, "ContentsStatus")) ? NewsState.I
                      : NewsState.U);
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
                  "H".equals(getElementValue(newsElement, "ContentsType")) ? NewsType.H : NewsType.T);
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