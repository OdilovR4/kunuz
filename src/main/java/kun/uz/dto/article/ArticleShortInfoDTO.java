package kun.uz.dto.article;

import kun.uz.dto.attach.AttachDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleShortInfoDTO {
    private String id;
    private String title;
    private String description;
    private AttachDTO photo;
    private LocalDateTime publishedDate;
}
