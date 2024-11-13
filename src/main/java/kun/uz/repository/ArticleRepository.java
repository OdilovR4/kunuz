package kun.uz.repository;

import jakarta.transaction.Transactional;
import kun.uz.dto.article.ArticleShortInfoMapper;
import kun.uz.entity.ArticleEntity;
import kun.uz.enums.ArticleStatus;
import org.hibernate.mapping.Selectable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ArticleRepository extends CrudRepository<ArticleEntity,String>, PagingAndSortingRepository<ArticleEntity,String> {

    @Modifying
    @Transactional
    @Query("Update ArticleEntity set visible = false where id = ?1")
    int deleteArticle(String id);

    @Query("from ArticleEntity where id = ?1 and visible = true")
    ArticleEntity getById(String id);

    @Query("select a.id as id, a.title as title, a.description as description, a.photoId as photoId, a.publishedDate as publishDate " +
            "From ArticleEntity as a where a.id in ?2 and a.status = ?1 and a.visible = true ")
    List<ArticleShortInfoMapper> getByType(ArticleStatus articleStatus, List<String> stringList, Pageable of);


    @Query("Select  a.id as id, a.title as title, a.description as description, a.photoId as photoId, a.publishedDate as publishDate " +
            "From ArticleEntity as a where a.id not in ?1 and a.status = ?2 and a.visible = true order by a.publishedDate desc ")
    List<ArticleShortInfoMapper> getLast8NotIn(List<String> articleIds, ArticleStatus articleStatus, Pageable of);

    @Query("Select  a.id as id, a.title as title, a.description as description, a.photoId as photoId, a.publishedDate as publishDate " +
            "From ArticleEntity as a where a.status = ?1 and a.visible = true order by a.viewCount desc ")
    List<ArticleShortInfoMapper> getMostRead(ArticleStatus articleStatus, Pageable of);

    @Query("Select  a.id as id, a.title as title, a.description as description, a.photoId as photoId, a.publishedDate as publishDate " +
            "From ArticleEntity as a where a.description ilike %?1% and a.status = ?2 and a.visible = true order by a.publishedDate desc ")
    List<ArticleShortInfoMapper> getByTage(String tag, ArticleStatus articleStatus);


    @Query("Select  a.id as id, a.title as title, a.description as description, a.photoId as photoId, a.publishedDate as publishDate " +
            "From ArticleEntity as a where a.status = ?1 and a.visible = true and a.regionId = ?2 order by a.publishedDate desc ")
    Page<ArticleShortInfoMapper> getByRegion(ArticleStatus articleStatus, String regionId, Pageable of);


    @Query("Select  a.id as id, a.title as title, a.description as description, a.photoId as photoId, a.publishedDate as publishDate " +
            "From ArticleEntity as a where a.status = ?1 and a.visible = true and a.categoryId = ?2 order by a.publishedDate desc ")
    Page<ArticleShortInfoMapper> getByCategory(ArticleStatus articleStatus, Integer categoryId, Pageable pageable);


    @Modifying
    @Transactional
    @Query("Update ArticleEntity Set sharedCount = sharedCount + 1 where id = ?1")
    void sharedCount(String articleId);
}
