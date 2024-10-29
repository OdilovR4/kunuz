package kun.uz.controller;

import jakarta.validation.Valid;
import kun.uz.dto.FilterDTO;
import kun.uz.dto.ProfileCreationDTO;
import kun.uz.dto.ProfileDTO;
import kun.uz.service.AuthService;
import kun.uz.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;
    @Autowired
    AuthService authService;

    @PostMapping
    public ResponseEntity<ProfileDTO> createProfile(@Valid @RequestBody ProfileCreationDTO dto,
                                                    @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(profileService.create(dto, token));
    }


    @PutMapping("update/by-admin/{id}")
    public ResponseEntity<?> updateProfileByAdmin(@PathVariable Integer id,
                                           @Valid @RequestBody ProfileCreationDTO dto,
                                           @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(profileService.updateByAdmin(id,dto,token));
    }
    @PutMapping("update/by-own/{id}")
    public ResponseEntity<?> updateProfileByOwn(@PathVariable Integer id,
                                                  @Valid @RequestBody ProfileCreationDTO dto) {
        return ResponseEntity.ok(profileService.updateByOwn(id,dto));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProfileDTO>> getAllProfiles(@RequestParam Integer page, @RequestParam Integer size) {
        page = Math.max(0,page-1);
        return ResponseEntity.ok(profileService.getAll((page),size));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Integer id) {
        return ResponseEntity.ok(profileService.delete(id));
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<ProfileDTO>> filter(@RequestParam Integer page,
                                                   @RequestParam Integer size,
                                                   @RequestBody FilterDTO dto) {
        page = Math.max(0,page-1);
        return ResponseEntity.ok(profileService.filter((page),size,dto));

    }
}
