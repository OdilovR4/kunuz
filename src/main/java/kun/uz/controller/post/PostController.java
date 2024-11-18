package kun.uz.controller.post;

import kun.uz.dto.post.PostDTO;
import kun.uz.enums.AppLanguage;
import kun.uz.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/post")
@RestController
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public ResponseEntity<PostDTO> create (@RequestBody PostDTO postDTO) {
        return ResponseEntity.ok(postService.create(postDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<Page> getAll(@RequestParam int page,
                                       @RequestParam int size) {
        page = Math.max(page - 1, 0);
        return ResponseEntity.ok(postService.getAll(page,size));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete (@PathVariable Integer id) {
        return ResponseEntity.ok(postService.delete(id));

    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> update (@PathVariable Integer id,
                                     @RequestBody PostDTO postDTO,
                                     @RequestHeader("Accepted-Language") AppLanguage language) {
        return ResponseEntity.ok(postService.update(id,postDTO, language.name()));
    }



}
