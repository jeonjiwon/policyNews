package type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  NOT_FOUND_DATA("데이터를 찾을 수 없습니다.");

  private final String description;
}
