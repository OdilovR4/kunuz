package kun.uz.controller.attach;

import kun.uz.dto.attach.AttachDTO;
import kun.uz.enums.AppLanguage;
import kun.uz.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attach")
public class AttachController {
    @Autowired
    private AttachService attachService;

//    @PostMapping("/upload")
//    public String upload(@RequestParam("file") MultipartFile file) {
//        return attachService.saveToService(file);
//    }

    @GetMapping("/open/{file}")
    public ResponseEntity<Resource> open(@PathVariable String file,
                                         @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage lang) {
        return attachService.open(file,lang.name());
    }

    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file,
                                            @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage lang) {
        return ResponseEntity.ok(attachService.upload(file, lang.name()));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id,
                                             @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage lang) {
        return attachService.download(id, lang.name());

    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> all(@RequestParam int page,
                                 @RequestParam int size) {
        page = Math.max(page - 1, 0);
        return ResponseEntity.ok(attachService.getAll(page, size));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage lang) {
        return ResponseEntity.ok(attachService.delete(id, lang.name()));
    }


}
