package kun.uz.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmailHistoryDTO {
    Integer id;
    String email;
    String message;
    LocalDateTime createdDate;
}
