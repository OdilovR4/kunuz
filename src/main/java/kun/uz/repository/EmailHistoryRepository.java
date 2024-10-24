package kun.uz.repository;

import kun.uz.entity.EmailHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity,Integer> {



    List<EmailHistoryEntity> findAllByEmail(String email);


    @Query("from EmailHistoryEntity where createdDate between ?1 and ?2 ")
    List<EmailHistoryEntity> getByGivenDate(LocalDateTime from, LocalDateTime to);


    @Query("from EmailHistoryEntity order by createdDate desc ")
    Page<EmailHistoryEntity> getByPagination(Pageable pageable)
            ;
}
