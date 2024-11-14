package kun.uz.dto.filter;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Service
public class CommentFilterDTO {
  private String id;
  private LocalDate from;
  private LocalDate to;
  private Integer profileId;
  private String articleId;
}
