package kun.uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "post_attach")
public class PostAttachEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "post_id")
    Integer postId;

    @Column(name = "attach_id")
    String  attachId;

    @Column(name = "created_date")
    LocalDateTime createdDate;



}
