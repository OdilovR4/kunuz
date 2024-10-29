package kun.uz.controller;

import jakarta.validation.Valid;
import kun.uz.dto.ArticleTypeDTO;
import kun.uz.dto.NameInterface;
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

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody ArticleTypeDTO dto,
                                    @RequestHeader("Authorization") String token){
     return ResponseEntity.ok(articleTypeService.create(dto, token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody ArticleTypeDTO dto,
                                    @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(articleTypeService.update(id,dto, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id,
                                         @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(articleTypeService.delete(id, token));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ArticleTypeDTO>> getAll(@RequestParam Integer page,
                                                       @RequestParam Integer size,
                                                       @RequestHeader("Authorization") String token){
        page = Math.max(0,page-1);
        return ResponseEntity.ok(articleTypeService.getAll(page,size,token));
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
