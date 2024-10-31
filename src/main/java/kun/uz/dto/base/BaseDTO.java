package kun.uz.dto.base;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class BaseDTO {
    private Integer id;
    @NotNull(message = "orderNumber is required")
    private Integer orderNumber;
    @NotBlank(message = " data in uzbek language is required")
    private String nameUz;
    @NotBlank(message = " data in russian language is required")
    private String nameRu;
    @NotBlank(message = " data in english language is required")
    private String nameEn;
    private LocalDateTime createdDate;

}
