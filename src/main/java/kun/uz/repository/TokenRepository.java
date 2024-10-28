package kun.uz.repository;

import kun.uz.entity.TokenEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TokenRepository extends CrudRepository<TokenEntity,Integer> {

    @Query("from TokenEntity order by id asc ")
    List<TokenEntity> getByDate();
}
