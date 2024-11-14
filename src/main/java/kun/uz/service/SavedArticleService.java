package kun.uz.service;

import kun.uz.config.CustomUserDetails;
import kun.uz.dto.SavedArticleDTO;
import kun.uz.dto.article.ArticleDTO;
import kun.uz.entity.ArticleEntity;
import kun.uz.entity.SavedArticleEntity;
import kun.uz.repository.SavedArticleRepository;
import kun.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class SavedArticleService {

    @Autowired
    private SavedArticleRepository savedArticleRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private AttachService attachService;

    public SavedArticleDTO create(SavedArticleDTO dto) {
        CustomUserDetails profile = SpringSecurityUtil.getCurrentUser();
        if(!isExist(dto.getArticleId(), profile.getId())){
            SavedArticleEntity entity = new SavedArticleEntity();
            entity.setArticleId(dto.getArticleId());
            entity.setProfileId(profile.getId());
            entity.setCreatedDate(LocalDateTime.now());
            savedArticleRepository.save(entity);
        }
        dto.setProfileId(profile.getId());
        return dto;

    }
    public boolean isExist(String articleId, Integer profileId) {
        return savedArticleRepository.isExist(articleId,profileId)!=null;
    }

    public void delete(Integer id) {
         savedArticleRepository.deleteById(id);
    }
    public List<SavedArticleDTO> getProfileArticleList() {
        Integer userId = SpringSecurityUtil.getCurrentUser().getId();
        List<SavedArticleEntity> entities = savedArticleRepository.getByUserId(userId);
        List<SavedArticleDTO> dtos = new ArrayList<>();
        for (SavedArticleEntity entity : entities) {
            dtos.add(fix(entity));
        }
        return dtos;
    }

    public SavedArticleDTO fix(SavedArticleEntity entity) {
        SavedArticleDTO dto = new SavedArticleDTO();
        dto.setId(entity.getId());
        dto.setArticle(fixArticle(entity.getArticle()));
        return dto;
    }

    public ArticleDTO fixArticle(ArticleEntity article) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        dto.setViewCount(article.getViewCount());
        dto.setSharedCount(article.getSharedCount());
        dto.setPhoto(attachService.getDto(article.getPhotoId()));
        return dto;

    }
}
