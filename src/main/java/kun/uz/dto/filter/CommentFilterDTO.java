package kun.uz.dto.filter;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Getter
@Service
public class CommentFilterDTO {
  private String id;
  private LocalDateTime from;
  private LocalDateTime to;
  private Integer profile_id;
  private String article_id;
}
