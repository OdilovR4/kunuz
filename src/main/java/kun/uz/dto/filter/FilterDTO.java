package kun.uz.dto.filter;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FilterDTO {
    private String name;
    private String surname;
    private String login;
    private String role;
    private LocalDate from;
    private LocalDate to;
}
