package kun.uz.service;


import kun.uz.dto.article.ArticleTypeDTO;
import kun.uz.entity.ArticleEntity;
import kun.uz.entity.ArticleTypeArticleEntity;
import kun.uz.repository.ArticleRepository;
import kun.uz.repository.ArticleTypeArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleTypeArticleService {
    @Autowired
    private ArticleTypeArticleRepository articleTypeArticleRepository;
    @Autowired
    private ArticleTypeService articleTypeService;

    public void addArticleType(String articleId, List<Integer> articleTypeList) {
        for (Integer art : articleTypeList) {
            ArticleTypeArticleEntity entity = new ArticleTypeArticleEntity();
            entity.setArticleId(articleId);
            entity.setArticleTypeId(art);
            entity.setCreatedDate(LocalDateTime.now());
            articleTypeArticleRepository.save(entity);
        }

    }

    public List<ArticleTypeDTO> getArticleList(String id) {
        List<Integer> articleTypeId = articleTypeArticleRepository.getTypes(id);
        List<ArticleTypeDTO> articleTypeDTOList = new ArrayList<>();
        for (Integer i : articleTypeId) {
            articleTypeDTOList.add(articleTypeService.getById(i));
        }
        return articleTypeDTOList;
    }

    public List<String> getArticlesByTypes(Integer articleTypeId) {
        List<String> articleTypes = articleTypeArticleRepository.getByArticleTypes(articleTypeId);
        if (articleTypes.isEmpty()) {
            return null;
        }
        return articleTypes;
    }
}
