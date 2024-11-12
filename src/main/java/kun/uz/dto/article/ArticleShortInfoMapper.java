package kun.uz.dto.article;

import java.time.LocalDateTime;

public interface ArticleShortInfoMapper {
    String getId();

    String getTitle();

    String getDescription();

    String getPhotoId();

    LocalDateTime getPublishDate();

}
