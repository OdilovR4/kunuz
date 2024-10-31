package kun.uz.controller.article;

import jakarta.validation.Valid;
import kun.uz.dto.article.ArticleTypeDTO;
import kun.uz.dto.base.NameInterface;
import kun.uz.service.ArticleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("article")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody ArticleTypeDTO dto){
     return ResponseEntity.ok(articleTypeService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ArticleTypeDTO dto){
        return ResponseEntity.ok(articleTypeService.update(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id){
        return ResponseEntity.ok(articleTypeService.delete(id));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ArticleTypeDTO>> getAll(@RequestParam(defaultValue = "0") Integer page,
                                                       @RequestParam(defaultValue = "10") Integer size){

        page = Math.max(0,page-1);
        return ResponseEntity.ok(articleTypeService.getAll(page,size));
    }

  /*  @GetMapping("/lang")
    public ResponseEntity<List<NameInterface>> getAll(@RequestParam String lang){
        return ResponseEntity.ok(articleTypeService.getByLang(lang));
    }*/

    @GetMapping("/get-by-lang")
    public ResponseEntity<List<NameInterface>> getByLang(@RequestHeader(value = "Accept-language",
            defaultValue = "uz") String language) {
        return ResponseEntity.ok(articleTypeService.getByLang(language));
    }



}
