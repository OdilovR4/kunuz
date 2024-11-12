package kun.uz.repository;

import kun.uz.entity.ArticleEntity;
import kun.uz.entity.ArticleTypeArticleEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ArticleTypeArticleRepository extends CrudRepository<ArticleTypeArticleEntity,Integer>,
                                                      PagingAndSortingRepository<ArticleTypeArticleEntity,Integer> {

    @Query("select articleTypeId from ArticleTypeArticleEntity where articleId = ?1")
    List<Integer> getTypes(String id);

    @Query("Select a.articleId from ArticleTypeArticleEntity as a where a.articleTypeId = ?1 order by a.createdDate desc ")
    List<String> getByArticleTypes(Integer articleTypeId);
}
