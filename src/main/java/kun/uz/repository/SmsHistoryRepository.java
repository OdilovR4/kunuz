package kun.uz.repository;

import jakarta.transaction.Transactional;
import kun.uz.entity.SmsHistoryEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SmsHistoryRepository extends CrudRepository<SmsHistoryEntity,Integer> {
    @Query("from SmsHistoryEntity where phone = ?1 order by sendTime desc")
    List<SmsHistoryEntity> getByPhone(String phone);

    @Query("select count (s) from SmsHistoryEntity s where s.phone = ?1 and s.sendTime between ?2 and ?3")
    Long getCountSms(String phone, LocalDateTime time, LocalDateTime time2);

    @Modifying
    @Transactional
    @Query("Update SmsHistoryEntity set attemptCount = attemptCount + 1 where id = ?1 ")
    void increaseAttempt(Integer id);
}
