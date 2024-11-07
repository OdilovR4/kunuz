package kun.uz.repository;

import jakarta.transaction.Transactional;
import kun.uz.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<PostEntity,Integer>,
                                        PagingAndSortingRepository<PostEntity,Integer> {


    @Query("From PostEntity where visible = true")
    Page<PostEntity> getAllAndVisibleTrue(Pageable pageable);

    @Modifying
    @Transactional
    @Query("Update PostEntity Set visible = false where id = :postId")
    int deletePost(@Param("postId") Integer postId);
}
