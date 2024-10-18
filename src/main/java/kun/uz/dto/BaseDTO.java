package kun.uz.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class BaseDTO {
    private Integer id;
    @NotNull(message = "orderNumber is required")
    private Integer orderNumber;
    @NotNull(message = " data in uzbek language is required")
    private String nameUz;
    @NotNull(message = " data in russian language is required")
    private String nameRu;
    @NotNull(message = " data in english language is required")
    private String nameEn;
    private Boolean visible;
    private LocalDateTime createdDate;
}
