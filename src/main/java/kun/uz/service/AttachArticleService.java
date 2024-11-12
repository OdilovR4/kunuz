package kun.uz.service;

import kun.uz.dto.attach.AttachDTO;
import kun.uz.entity.AttachArticleEntity;
import kun.uz.entity.PostAttachEntity;
import kun.uz.repository.AttachArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttachArticleService {
    @Autowired
    private AttachArticleRepository attachArticleRepository;
    @Autowired
    private AttachService attachService;


    public void addAttach(String articleId, List<AttachDTO> imagesId) {
        for (AttachDTO attach : imagesId) {
            AttachArticleEntity entity = new AttachArticleEntity();
            entity.setArticleId(articleId);
            entity.setAttachId((attach.getId()));
            entity.setCreatedTime(LocalDateTime.now());
            attachArticleRepository.save(entity);
        }
    }

    public List<AttachDTO> getAttachList(String id) {
        List<String> attachList = attachArticleRepository.getAttaches(id);
        List<AttachDTO> attachDTOList = new ArrayList<>();
        for (String attachId : attachList) {
            attachDTOList.add(attachService.getDto(attachId));
        }
        return attachDTOList;
    }

    public void updateAttach(String articleId, List<AttachDTO> newImageList) {
        if (newImageList == null) {
            newImageList = new ArrayList<>();
        }
        List<String> oldImageList = attachArticleRepository.getAttaches(articleId);
        for (String attachId : oldImageList) {
            if (!isExist(attachId, newImageList)) {
                attachArticleRepository.deleteArticleAndAttach(articleId, attachId);
            }
        }

        for (AttachDTO attach : newImageList) {
            if (!oldImageList.contains(attach.getId())) {
                AttachArticleEntity entity = new AttachArticleEntity();
                entity.setArticleId(articleId);
                entity.setAttachId(attach.getId());
                entity.setCreatedTime(LocalDateTime.now());
                attachArticleRepository.save(entity);
            }
        }

    }

    public boolean isExist(String attachId, List<AttachDTO> imagesId) {
        for (AttachDTO attach : imagesId) {
            if (attach.getId().equals(attachId)) {
                return true;
            }
        }
        return false;
    }
}
