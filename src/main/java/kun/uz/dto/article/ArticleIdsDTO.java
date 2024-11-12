package kun.uz.dto.article;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleIdsDTO {

    List<String> articleIds;
    List<ArticleDTO> articlesDTO;


}
