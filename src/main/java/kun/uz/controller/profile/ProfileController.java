package kun.uz.controller.profile;

import jakarta.validation.Valid;
import kun.uz.dto.filter.FilterDTO;
import kun.uz.dto.base.JwtDTO;
import kun.uz.dto.profile.ProfileCreationDTO;
import kun.uz.dto.profile.ProfileDTO;
import kun.uz.dto.profile.UpdateProfileDetail;
import kun.uz.service.ProfileService;
import kun.uz.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfileDTO> createProfile(@Valid @RequestBody ProfileCreationDTO dto) {
        return ResponseEntity.ok(profileService.create(dto));
    }

    @PutMapping("update/by-admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProfileByAdmin(@PathVariable Integer id,
                                           @Valid @RequestBody ProfileCreationDTO dto) {
        return ResponseEntity.ok(profileService.updateByAdmin(id,dto));
    }

    @PutMapping("/update/by-own")
    public ResponseEntity<?> updateProfileByOwn(@Valid @RequestBody UpdateProfileDetail dto,
                                                @RequestHeader("Authorization") String token) {
        JwtDTO jwtDTO = JwtUtil.decode(token);
        return ResponseEntity.ok(profileService.updateByOwn(dto, jwtDTO.getUsername()));
    }

    @GetMapping("/all")
   @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ProfileDTO>> getAllProfiles(@RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        page = Math.max(0,page-1);
        return ResponseEntity.ok(profileService.getAll((page),size));
    }

    @PutMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProfile(@PathVariable Integer id) {
        return ResponseEntity.ok(profileService.delete(id));
    }

    @PostMapping("/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ProfileDTO>> filter(@RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer size,
                                                   @RequestBody FilterDTO dto) {
        page = Math.max(0,page-1);
      //  HttpStatus
        return ResponseEntity.ok(profileService.filter((page),size,dto));
    }
}
