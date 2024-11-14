package kun.uz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kun.uz.dto.article.ArticleDTO;
import kun.uz.dto.profile.ProfileDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SavedArticleDTO {
    private Integer id;
    private Integer profileId;
    private String articleId;
    private ArticleDTO article;
    private ProfileDTO entity;
    private LocalDateTime createdDate;
}
