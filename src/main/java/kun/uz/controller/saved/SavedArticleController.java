package kun.uz.controller.saved;

import kun.uz.dto.SavedArticleDTO;
import kun.uz.service.SavedArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("saved")
public class SavedArticleController {
    @Autowired
    private SavedArticleService savedArticleService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SavedArticleDTO dto) {
        return ResponseEntity.ok(savedArticleService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        savedArticleService.delete(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);

    }

    @GetMapping
    public ResponseEntity<?> getProfileArticleList() {
        return ResponseEntity.ok(savedArticleService.getProfileArticleList());
    }

}
