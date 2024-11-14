package kun.uz.repository;

import kun.uz.entity.ArticleEntity;
import kun.uz.entity.ArticleLikeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleLikeRepository extends CrudRepository<ArticleLikeEntity,Integer> {


    @Query("from ArticleLikeEntity where articleId = ?1 and profileId = ?2 ")
    ArticleLikeEntity iLike(String articleId, Integer profileId);

    @Query("select articleId from ArticleLikeEntity where profileId = ?1 ")
    String hello(Integer profileId);
}
