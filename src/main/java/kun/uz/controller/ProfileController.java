package kun.uz.controller;

import jakarta.validation.Valid;
import kun.uz.dto.FilterDTO;
import kun.uz.dto.ProfileDTO;
import kun.uz.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @PostMapping
    public ResponseEntity<?> createProfile(@Valid @RequestBody ProfileDTO profile) {
        return ResponseEntity.ok(profileService.create(profile));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Integer id, @Valid @RequestBody ProfileDTO profile) {
        return ResponseEntity.ok(profileService.update(id,profile));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProfileDTO>> getAllProfiles(@RequestParam Integer page, @RequestParam Integer size) {
        return ResponseEntity.ok(profileService.getAll((page-1),size));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Integer id) {
        return ResponseEntity.ok(profileService.delete(id));
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<ProfileDTO>> filter(@RequestParam Integer page,
                                                   @RequestParam Integer size,
                                                   @RequestBody FilterDTO dto) {
        return ResponseEntity.ok(profileService.filter((page-1),size,dto));

    }
}
