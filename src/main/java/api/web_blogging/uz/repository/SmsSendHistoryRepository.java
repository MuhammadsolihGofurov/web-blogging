package api.web_blogging.uz.repository;


import api.web_blogging.uz.entity.SmsHistoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SmsSendHistoryRepository extends CrudRepository<SmsHistoryEntity, String> {

    Long countByPhoneAndCreatedAtBefore(String phone, LocalDateTime from, LocalDateTime to);
}
