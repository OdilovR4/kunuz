package kun.uz.repository;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kun.uz.entity.ProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Repository
public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer>,
        PagingAndSortingRepository<ProfileEntity, Integer> {

    @Query(value = "From ProfileEntity as p where p.visible = true order by p.id asc")
    Page<ProfileEntity> findAllByVisibleTrue(Pageable pageable);

    @Query(value = "From ProfileEntity as p where p.visible = true order by p.id asc")
    List<ProfileEntity> getAll();


    @Modifying
    @Transactional
    @Query("update ProfileEntity set visible = false where id = ?1")
    int deleteProfile(Integer id);

    @Query(value = "From ProfileEntity where id = ?1 and visible = true")
    ProfileEntity findByIdAndVisibleTrue(Integer id);


    ProfileEntity getByEmailAndVisibleTrue(String email);
}
