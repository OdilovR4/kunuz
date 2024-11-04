package kun.uz.repository;

import kun.uz.entity.AttachEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachRepository extends CrudRepository<AttachEntity, String> {


    @Query("from AttachEntity where id = ?1 and visible = true")
    Optional<AttachEntity> findByIdAndVisibleTrue(String id);
}
