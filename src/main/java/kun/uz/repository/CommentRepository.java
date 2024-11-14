package kun.uz.repository;

import jakarta.transaction.Transactional;
import kun.uz.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<CommentEntity,String > {

    @Modifying
    @Transactional
    @Query("Update CommentEntity set visible = false where id = ?1")
    Integer deleteArticle(String commentId);

    @Query("From CommentEntity where articleId = ?1 and visible = true order by createdDate desc ")
    List<CommentEntity> getBYArticleId(String articleId);


    @Query("from CommentEntity where articleId = ?1 and visible = true order by createdDate desc ")
    Page<CommentEntity> getByPage(String articleId, Pageable pageable);

    @Query("From CommentEntity where replyId = ?1 and visible = true")
    List<CommentEntity> getReplied(String commentId);
}
