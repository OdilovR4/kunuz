package kun.uz.entity;

import jakarta.persistence.*;
import kun.uz.enums.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "article")
public class ArticleEntity {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private String id;
    private String title;
    private String description;
    private String content;
    @Column(name = "shared_count")
    private long sharedCount;
    @Column(name = "view_count")
    private long viewCount;
    @Column(name = "image_id")
    private String imageId;
    @Column(name = "region_id")
    private Integer regionId;
    @Column(name = "category_id")
    private Integer categoryId;
    @Column(name = "moderator_id")
    private Integer moderatorId;
    @Column(name = "publisher_id")
    private Integer publisherId;
    private ArticleStatus status;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    private Boolean visible;
}
