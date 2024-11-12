package kun.uz.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateArticleDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private Long sharedCount;
    private String photoId;
    private Integer regionId;
    private Integer categoryId;
}
