package api.web_blogging.uz.repository;


import api.web_blogging.uz.entity.SmsHistoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SmsSendHistoryRepository extends CrudRepository<SmsHistoryEntity, String> {

    Long countByPhoneAndCreatedAtBefore(String phone, LocalDateTime from, LocalDateTime to);

    Optional<SmsHistoryEntity> findTop1ByPhoneAndCreatedAtDesc(String phone);

    @Modifying
    @Transactional
    @Query("update SmsHistoryEntity set attemptCount = coalesce(attemptCount, 0) + 1 where id =?1")
    void updateAttemptCount(String id);
}
