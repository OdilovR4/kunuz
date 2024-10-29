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
    Page<EmailHistoryEntity> getByPagination(Pageable pageable);


    @Query("Select count(e) from EmailHistoryEntity e where e.email = ?1 and e.createdDate between ?2 and ?3")
    Long getEmailCount(String to, LocalDateTime localDateTime, LocalDateTime now);

    @Query("from EmailHistoryEntity where email = ?1 order by createdDate desc")
    List<EmailHistoryEntity> getLastSentEmail(String email);
}
