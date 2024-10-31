package kun.uz.controller.region;

import kun.uz.dto.region.RegionDTO;
import kun.uz.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("region")
public class RegionController {
    @Autowired
    RegionService regionService;


    @PostMapping("")
    public ResponseEntity<RegionDTO> createRegion(@RequestBody RegionDTO region) {
        return ResponseEntity.ok(regionService.createRegion(region));

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRegion(@PathVariable Integer id, @RequestBody RegionDTO region) {
        return ResponseEntity.ok(regionService.updateRegion(id,region));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegion(@PathVariable Integer id) {
        return ResponseEntity.ok(regionService.delete(id));
    }

    @GetMapping
    public ResponseEntity<List<RegionDTO>>getAllRegions() {
        return ResponseEntity.ok(regionService.getAll());
    }

    @GetMapping("/lang")
    public ResponseEntity<List<?>> getByLang(@RequestParam String lang) {
        return ResponseEntity.ok(regionService.getByLang(lang));
    }

}
