package kun.uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Table(name = "post")
@Entity
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String title;
    Boolean visible;

    @OneToMany(mappedBy = "post")
    private List<PostAttachEntity> attaches;

    @Column(name = "created_date")
    LocalDateTime createdDate;
}
