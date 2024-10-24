package kun.uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "email_history")
public class EmailHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "email")
    String email;
    @Column(name = "message",columnDefinition = "TEXT")
    String message;
    @Column(name = "created_date")
    LocalDateTime createdDate;



}
