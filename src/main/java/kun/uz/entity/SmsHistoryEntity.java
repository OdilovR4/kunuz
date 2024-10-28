package kun.uz.entity;

import jakarta.persistence.*;
import kun.uz.enums.SmsStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "sms_history")
@Entity
@Getter
@Setter
public class SmsHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String phone;
    private String message;
    private Integer smsCode;
    private SmsStatus status;
    private LocalDateTime sendTime;
    private Integer attemptCount;
}
