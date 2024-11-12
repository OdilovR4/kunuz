package kun.uz.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import kun.uz.dto.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleTypeDTO extends BaseDTO {

}
