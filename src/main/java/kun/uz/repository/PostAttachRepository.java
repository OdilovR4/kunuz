package kun.uz.repository;

import jakarta.transaction.Transactional;
import kun.uz.entity.PostAttachEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostAttachRepository extends CrudRepository<PostAttachEntity,Integer> {

    @Query(" Select attachId from  PostAttachEntity where postId = ?1")
    List<String> getPhotosById(int postId);


    @Modifying
    @Transactional
    @Query("delete from PostAttachEntity where postId = ?1")
    int deleteAllPhotos(Integer postId);
}
