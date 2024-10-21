package kun.uz.entity;

import jakarta.persistence.*;
import jdk.jfr.BooleanFlag;
import kun.uz.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "profile")
@Getter
@Setter
public class ProfileEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Integer id;
    @Column(name = "name")
    String name;
    @Column(name = "surname")
    String surname;
    @Column(name = "login")
    String login;
    @Column(name = "password")
    String password;
    @Column(name = "role")
    String role;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    ProfileStatus status;
    @Column(name = "visible")
    Boolean visible;
    @Column(name = "created_date")
    LocalDateTime createdDate;
    @Column(name = "photo_id")
    Integer photo_id;

}
