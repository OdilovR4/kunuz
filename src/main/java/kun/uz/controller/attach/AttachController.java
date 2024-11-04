package kun.uz.controller.attach;

import kun.uz.dto.attach.AttachDTO;
import kun.uz.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Resource> open(@PathVariable String file) {
        return attachService.open(file);
    }

    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachService.upload(file));
    }

}
