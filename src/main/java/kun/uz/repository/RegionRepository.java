package kun.uz.repository;

import jakarta.transaction.Transactional;
import kun.uz.dto.NameInterface;
import kun.uz.entity.RegionEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegionRepository extends CrudRepository<RegionEntity, Integer> {

    @Modifying
    @Transactional
    @Query(value = "delete from RegionEntity where id = ?1")
    int deleteRegion(int id);

    @Query("From RegionEntity ")
    List<RegionEntity>allRegions();


    @Query("select r.id as id, "+
       "CASE "+
           "WHEN ?1='uz' THEN r.nameUz "+
           "WHEN ?1='ru' THEN r.nameRu "+
           "WHEN ?1='en' THEN r.nameEn "+
       "END "+
            "as name "+
            "From RegionEntity as r where r.visible = true order by r.orderNumber asc" )

    List<NameInterface> getByLang(String lang);
}
