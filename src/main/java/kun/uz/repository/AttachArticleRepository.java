package kun.uz.repository;

import jakarta.transaction.Transactional;
import kun.uz.entity.AttachArticleEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttachArticleRepository extends CrudRepository<AttachArticleEntity, Integer> {

    @Query("select attachId from AttachArticleEntity where articleId = ?1")
    List<String> getAttaches(String id);


    @Modifying
    @Transactional
    @Query("delete from AttachArticleEntity where articleId = ?1 and attachId = ?2")
    void deleteArticleAndAttach(String articleId, String attachId);
}
