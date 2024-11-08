package kun.uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "attach")
public class AttachEntity {

    @Id
    private String id;

    @Column(name = "path")
    private String path;

    @Column(name = "extension")
    private String extension;

    @Column(name = "origen_name")
    private String origenName;
    @Column(name = "size")
    private Long size;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @OneToMany(mappedBy = "attach")
    private List<PostAttachEntity> posts;

    @Column(name = "visible")
    private Boolean visible = true;

}
