package kun.uz.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import kun.uz.dto.attach.AttachDTO;
import kun.uz.dto.category.CategoryDTO;
import kun.uz.dto.profile.ProfileDTO;
import kun.uz.dto.region.RegionDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private long sharedCount;
    private long viewCount;
    private AttachDTO photo;
    private String photoId;
    private List<ArticleTypeDTO> articleType;
    private RegionDTO region;
    private Integer regionId;
    private CategoryDTO category;
    private Integer categoryId;
    private ProfileDTO moderator;
    private Integer moderatorId;
    private ProfileDTO publisher;
    private Integer publisherId;
    private List<String> tags;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;
}
