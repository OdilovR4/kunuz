package kun.uz.dto.article;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleCreationDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private String photoId;
    private Integer regionId;
    private Integer categoryId;
    private List<Integer> articleType;
    private List<String> tags;

}
