package kun.uz.repository;

import kun.uz.entity.SavedArticleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedArticleRepository extends CrudRepository<SavedArticleEntity,Integer> {
    @Query("from SavedArticleEntity where articleId = ?1 and profileId = ?2")
    SavedArticleEntity isExist(String articleId, Integer profileId);

    @Query("From SavedArticleEntity where profileId = ?1")
    List<SavedArticleEntity> getByUserId(Integer userId);
}
