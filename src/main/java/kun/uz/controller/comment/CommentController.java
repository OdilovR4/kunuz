package kun.uz.controller.comment;

import kun.uz.dto.article.CommentDTO;
import kun.uz.dto.filter.CommentFilterDTO;
import kun.uz.enums.AppLanguage;
import kun.uz.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CommentDTO comment) {
        return ResponseEntity.ok(commentService.create(comment));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody CommentDTO comment,
                                    @RequestHeader(value = "Accepted-Language", defaultValue = "uz") AppLanguage language) {
        return ResponseEntity.ok(commentService.update(comment, language.name()));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/delete/{commentId}")
    public ResponseEntity<?> delete(@PathVariable String commentId) {
        return ResponseEntity.ok(commentService.delete(commentId));
    }


    @GetMapping("/by/{articleId}")
    public ResponseEntity<?> getCommentsByArticleId(@PathVariable String articleId) {
        return ResponseEntity.ok(commentService.getByArticleId(articleId));
    }

    @GetMapping("by-page/{articleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getByPagination(@PathVariable String articleId,
                                             @RequestParam Integer page,
                                             @RequestParam Integer size) {
        page = Math.max(page-1,0);
        return ResponseEntity.ok(commentService.getByPage(articleId, page, size));
    }

    @GetMapping("filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> filter(@RequestParam Integer page,
                                    @RequestParam Integer size,
                                    @RequestBody CommentFilterDTO filter) {
        page = Math.max(page-1,0);
        return ResponseEntity.ok(commentService.filter(page,size,filter));
    }

    @GetMapping("replied/{commentId}")
    public ResponseEntity<?> getReplied(@PathVariable String commentId) {
        return ResponseEntity.ok(commentService.getReplied(commentId));
    }






}
