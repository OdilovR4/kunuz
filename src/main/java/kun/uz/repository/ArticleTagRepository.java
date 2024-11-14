package kun.uz.repository;

import kun.uz.entity.ArticleTagEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleTagRepository extends CrudRepository<ArticleTagEntity, Integer> {

    @Query("Select tagName from ArticleTagEntity where articleId = ?1 order by createdDate desc")
    List<String> getTags(String id);
}
