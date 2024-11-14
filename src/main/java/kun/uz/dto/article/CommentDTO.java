package kun.uz.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import kun.uz.dto.profile.ProfileDTO;
import kun.uz.entity.ArticleEntity;
import kun.uz.entity.CommentEntity;
import kun.uz.entity.ProfileEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {

    private String id;
    private String content;
    private String articleId;
    private ArticleEntity article;
    private ArticleDTO articleDTO;
    private Integer profileId;
    private ProfileEntity profile;
    private ProfileDTO profileDTO;
    private String replyId;
    private CommentEntity reply;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
