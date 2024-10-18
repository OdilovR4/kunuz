package kun.uz.controller;

import kun.uz.dto.ArticleTypeDTO;
import kun.uz.dto.NameInterface;
import kun.uz.service.ArticleTypeService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("article")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody ArticleTypeDTO dto){
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
    public ResponseEntity<Page<ArticleTypeDTO>> getAll(@RequestParam Integer page, @RequestParam Integer size){
        return ResponseEntity.ok(articleTypeService.getAll((page-1),size));
    }

    @GetMapping("/lang")
    public ResponseEntity<List<NameInterface>> getAll(@RequestParam String lang){
        return ResponseEntity.ok(articleTypeService.getByLang(lang));
    }


}
