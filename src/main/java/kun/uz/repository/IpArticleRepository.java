package kun.uz.repository;

import kun.uz.controller.attach.IpArticleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IpArticleRepository extends CrudRepository<IpArticleEntity,Integer> {


    @Query("from IpArticleEntity where ip = ?1 and articleId = ?2")
    IpArticleEntity isExist(String ip, String articleId);
}
