package kun.uz.service;

import kun.uz.entity.ArticleLikeEntity;
import kun.uz.enums.LikeStatus;
import kun.uz.repository.ArticleLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ArticleLikeService {
    @Autowired
    private ArticleLikeRepository articleLikeRepository;


    public void create(String articleId, Integer profileId, LikeStatus status) {
        ArticleLikeEntity entity = articleLikeRepository.iLike(articleId, profileId);
        if (entity != null) {
            if (entity.getStatus().equals(status)) {
                entity.setStatus(LikeStatus.REMOVED);
            }
            else {
                entity.setStatus(status);
            }
        }
        else {
            entity = new ArticleLikeEntity();
            entity.setArticleId(articleId);
            entity.setProfileId(profileId);
            entity.setStatus(status);
            entity.setCreatedDate(LocalDateTime.now());
        }
        articleLikeRepository.save(entity);
    }
}
