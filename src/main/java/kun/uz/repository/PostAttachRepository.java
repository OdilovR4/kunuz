package kun.uz.repository;

import kun.uz.entity.PostAttachEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostAttachRepository extends CrudRepository<PostAttachEntity,Integer> {

    @Query(" Select attachId from  PostAttachEntity where postId = ?1")
    List<String> getPhotosById(int postId);

    @Query("delete from PostAttachEntity where postId = ?1")
    int deleteAllPhotos(Integer postId);
}
