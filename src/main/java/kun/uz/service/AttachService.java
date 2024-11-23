package kun.uz.service;

import kun.uz.dto.attach.AttachDTO;
import kun.uz.entity.AttachEntity;
import kun.uz.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AttachService {

    @Autowired
    private AttachRepository attachRepository;

    private String folderName = "attaches";
    @Value("${server.domain}")
    private String domainName;
    @Autowired
    private ResourceBundleService resourceBundleService;

//    public String saveToService(MultipartFile file) {
//        File folder = new File("attaches");
//        if (!folder.exists()) {
//            folder.mkdir();
//        }
//        try {
//            byte [] bytes = file.getBytes();
//            Path path = Paths.get("attaches/" + file.getOriginalFilename());
//            Files.write(path, bytes);
//            return file.getOriginalFilename();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//
//    }

    public ResponseEntity<Resource> open(String id, String lang) {
        AttachEntity entity = getById(id,lang);
        Path filePath = Paths.get(folderName + "/" + entity.getPath() + "/" + entity.getId()).normalize();
        Resource resource = null;
        try {
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new RuntimeException(resourceBundleService.getMessage("file.not.found",lang) + id);
            }
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }


    public AttachDTO upload(MultipartFile file, String lang) {
        String pathFolder = getYmDString();
        String key = UUID.randomUUID().toString();
        String extension = getExtension(file.getOriginalFilename());

        File folder = new File(folderName + "/" + pathFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folderName + "/" + pathFolder + "/" + key + "." + extension);
            Files.write(path, bytes);

            AttachEntity attachEntity = new AttachEntity();
            attachEntity.setId(key + "." + extension);
            attachEntity.setPath(pathFolder);
            attachEntity.setSize(file.getSize());
            attachEntity.setVisible(true);
            attachEntity.setExtension(extension);
            attachEntity.setCreatedDate(LocalDateTime.now());
            attachEntity.setOrigenName(file.getOriginalFilename());

            attachRepository.save(attachEntity);

            return toDTO(attachEntity);
        } catch (IOException e) {
            throw new RuntimeException(resourceBundleService.getMessage("file.not.found",lang)  + file.getOriginalFilename());
        }

    }

    public ResponseEntity<Resource> download(String id,String lang) {
        AttachEntity entity = getById(id,lang);
        Path path = Paths.get(folderName + "/" + entity.getPath() + "/" + entity.getId()).normalize();
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOrigenName() + "\"").body(resource);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day;
    }

    private String getExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf("."); // mazgi.latta.jpg
        return fileName.substring(lastIndex + 1);
    }

    private AttachDTO toDTO(AttachEntity entity) {
        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(entity.getId());
        attachDTO.setOriginName(entity.getOrigenName());
        attachDTO.setSize(entity.getSize());
        attachDTO.setExtension(entity.getExtension());
        attachDTO.setCreatedData(entity.getCreatedDate());
        attachDTO.setUrl(getUrl(entity.getId()));

        return attachDTO;
    }

    private AttachEntity getById(String id, String lang) {
        return attachRepository.findByIdAndVisibleTrue(id).orElseThrow(() ->
                new RuntimeException(resourceBundleService.getMessage("file.not.found",lang) + id));
    }


    public Page<AttachDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AttachEntity> entityPage = attachRepository.getAll(pageable);
        List<AttachDTO> dtoList = new LinkedList<>();
        for (AttachEntity entity : entityPage) {
            dtoList.add(toDTO(entity));
        }
        return new PageImpl<>(dtoList, pageable, entityPage.getTotalPages());
    }

    public String delete(String id,String lang) {
        AttachEntity entity = getById(id,lang);
        attachRepository.delete(entity);
        return "Successfully deleted";
    }

    public String getUrl(String id) {
        return domainName + "/attach/open/" + id;
    }

    public AttachDTO getDto(String id) {
        if(id==null){
            return null;
        }
        AttachDTO dto = new AttachDTO();
        dto.setId(id);
        dto.setUrl(getUrl(id));
        return dto;
    }
}
