package kun.uz.service;

import kun.uz.entity.ArticleTagEntity;
import kun.uz.repository.ArticleTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleTagService {
    @Autowired
    private ArticleTagRepository articleTagRepository;

    public void createArticleTag(String id, List<String> tags) {
        for (String tag : tags) {
            ArticleTagEntity articleTagEntity = new ArticleTagEntity();
            articleTagEntity.setArticleId(id);
            articleTagEntity.setTagName(tag);
            articleTagRepository.save(articleTagEntity);
        }
    }

    public List<String> getTags(String id) {
        return articleTagRepository.getTags(id);

    }
}
