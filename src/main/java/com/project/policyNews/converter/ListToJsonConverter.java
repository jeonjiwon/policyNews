package com.project.policyNews.converter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Converter
public class ListToJsonConverter implements AttributeConverter<List<String>, String> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<String> attribute) {
    log.info("[tag커스텀 DB 저장을 위해 String 으로 변환");
    try {
      return objectMapper.writeValueAsString(attribute);  // List<String> -> JSON String
    } catch (Exception e) {
      throw new IllegalArgumentException("[커스텀] Error converting list to JSON: " + e.getMessage());
    }
  }

  @Override
  public List<String> convertToEntityAttribute(String dbData) {
    log.info("[tag커스텀 DB 에서 조회하여 List로 반환 ");
    try {
      return objectMapper.readValue(dbData, new TypeReference<List<String>>() {});  // JSON String -> List<String>
    } catch (Exception e) {
      throw new IllegalArgumentException("[커스텀] Error converting JSON to list: " + e.getMessage());
    }
  }
}
