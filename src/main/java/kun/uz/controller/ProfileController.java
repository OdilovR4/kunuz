package kun.uz.controller;

import jakarta.validation.Valid;
import kun.uz.dto.FilterDTO;
import kun.uz.dto.JwtDTO;
import kun.uz.dto.ProfileCreationDTO;
import kun.uz.dto.ProfileDTO;
import kun.uz.enums.ProfileRole;
import kun.uz.service.ProfileService;
import kun.uz.util.JwtUtil;
import kun.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;



    @PostMapping
    public ResponseEntity<ProfileDTO> createProfile(@Valid @RequestBody ProfileCreationDTO dto,
                                                    @RequestHeader("Authorization") String token) {
        validator(token);
        return ResponseEntity.ok(profileService.create(dto));

    }


    @PutMapping("update/by-admin/{id}")
    public ResponseEntity<?> updateProfileByAdmin(@PathVariable Integer id,
                                           @Valid @RequestBody ProfileCreationDTO dto,
                                           @RequestHeader("Authorization") String token) {
        validator(token);
        return ResponseEntity.ok(profileService.updateByAdmin(id,dto));
    }
    @PutMapping("update/by-own/{id}")
    public ResponseEntity<?> updateProfileByOwn(@PathVariable Integer id,
                                                  @Valid @RequestBody ProfileCreationDTO dto) {
        return ResponseEntity.ok(profileService.updateByOwn(id,dto));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProfileDTO>> getAllProfiles(@RequestParam Integer page,
                                                           @RequestParam Integer size,
                                                           @RequestHeader("Authorization") String token
    ) {
        validator(token);
        page = Math.max(0,page-1);
        return ResponseEntity.ok(profileService.getAll((page),size));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Integer id,
                                           @RequestHeader("Authorization") String token) {
        validator(token);
        return ResponseEntity.ok(profileService.delete(id));
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<ProfileDTO>> filter(@RequestParam Integer page,
                                                   @RequestParam Integer size,
                                                   @RequestBody FilterDTO dto,
                                                   @RequestHeader("Authorization") String token) {
       validator(token);
        page = Math.max(0,page-1);
        return ResponseEntity.ok(profileService.filter((page),size,dto));
        
    }
    public void validator(String token){
        JwtDTO dto1 = JwtUtil.decode(token);
        SpringSecurityUtil.checkRoleExists(dto1.getRole(), ProfileRole.ROLE_ADMIN);
    }
}
