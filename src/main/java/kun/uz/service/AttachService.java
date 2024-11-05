package kun.uz.service;

import kun.uz.dto.attach.AttachDTO;
import kun.uz.entity.AttachEntity;
import kun.uz.repository.AttachRepository;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
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

    public ResponseEntity<Resource> open(String id){
        AttachEntity entity = getById(id);
        Path filePath = Paths.get(folderName + "/"+entity.getPath()+"/"+entity.getId()).normalize();
        Resource resource = null;
        try {
            resource = new UrlResource(filePath.toUri());
            if(!resource.exists()){
                throw new RuntimeException("File not found: " + id);
            }
            String contentType = Files.probeContentType(filePath);
            if(contentType == null){
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }


    public AttachDTO upload(MultipartFile file) {
        String pathFolder = getYmDString();
        String key = UUID.randomUUID().toString();
        String extension = getExtension(file.getOriginalFilename());

        File folder = new File(folderName+"/"+pathFolder);
        if(!folder.exists()){
            folder.mkdirs();
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folderName + "/" + pathFolder + "/" + key + "." + extension);
            Files.write(path, bytes);

            AttachEntity attachEntity = new AttachEntity();
            attachEntity.setId(key+"."+extension);
            attachEntity.setPath(pathFolder);
            attachEntity.setSize(file.getSize());
            attachEntity.setVisible(true);
            attachEntity.setExtension(extension);
            attachEntity.setCreatedDate(LocalDateTime.now());
            attachEntity.setOrigenName(file.getOriginalFilename());
            attachRepository.save(attachEntity);

            return toDTO(attachEntity);
        }
        catch (IOException e) {
            throw new RuntimeException("File not found: " + file.getOriginalFilename());
        }

    }
    
    public Resource download(String id) {
        AttachEntity entity = getById(id);
        Path path =  Paths.get(folderName+"/"+entity.getPath()+"/"+entity.getId()).normalize();
        try {
            Resource resource = new UrlResource(path.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
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
        return attachDTO;
    }

    private AttachEntity getById(String id) {
        return attachRepository.findByIdAndVisibleTrue(id).orElseThrow(() -> new RuntimeException("File not found: " + id));
    }


    public Page<AttachDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AttachEntity> entityPage = attachRepository.getAll(pageable);
        List<AttachDTO> dtoList = new LinkedList<>();
        for(AttachEntity entity : entityPage){
            dtoList.add(toDTO(entity));
        }
        return new PageImpl<>(dtoList,pageable,entityPage.getTotalPages());
    }

    public String delete(String id) {
        AttachEntity entity = getById(id);
        attachRepository.delete(entity);
        return "Successfully deleted";
    }
}
