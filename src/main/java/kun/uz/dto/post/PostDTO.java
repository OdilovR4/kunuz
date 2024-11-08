package kun.uz.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostDTO {
    String title;
    List<String> imagesId;
}
