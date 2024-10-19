package kun.uz.repository;

import jakarta.transaction.Transactional;
import kun.uz.dto.NameOrder;
import kun.uz.entity.CategoryEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {

    @Modifying
    @Transactional
    @Query(value = "Delete from CategoryEntity where id = :idC")
    int deleteCategory(@Param("idC") Integer id);

    @Query("From CategoryEntity order by orderNumber asc")
    List<CategoryEntity> findAllCategories();

    @Query("select c.id as id, c.orderNumber as orderNumber, "+
      "CASE "+
              "WHEN :lang='uz' THEN c.nameUz "+
              "WHEN :lang='en' THEN c.nameEn "+
              "WHEN :lang='ru' THEN c.nameRu "+
      "END "+
              " as name "+
    "From CategoryEntity as c  WHERE c.visible = true order by c.orderNumber asc")
    List<NameOrder> getByLang(@Param("lang") String lang);
}
