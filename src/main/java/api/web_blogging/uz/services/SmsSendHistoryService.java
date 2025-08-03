package api.web_blogging.uz.services;


import api.web_blogging.uz.entity.SmsHistoryEntity;
import api.web_blogging.uz.enums.SmsType;
import api.web_blogging.uz.repository.SmsSendHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SmsSendHistoryService {
    @Autowired
    private SmsSendHistoryRepository smsSendHistoryRepository;


    public void create(String phone, String message, String code, SmsType smsType) {
        SmsHistoryEntity sms = new SmsHistoryEntity();
        sms.setPhone(phone);
        sms.setMessage(message);
        sms.setCode(code);
        sms.setAttemptCount(0);
        sms.setSmsType(smsType);
        sms.setCreatedAt(LocalDateTime.now());

        smsSendHistoryRepository.save(sms);
    }

    public Long getSmsCount(String phoneNumber) {
        LocalDateTime now = LocalDateTime.now();
        return smsSendHistoryRepository.countByPhoneAndCreatedAtBefore(phoneNumber, now.minusMinutes(1), now);
    }

    public Boolean check(String phoneNumber, String code) {
        Optional<SmsHistoryEntity> optional = smsSendHistoryRepository.findTop1ByPhoneAndCreatedAtDesc(phoneNumber);

        if (optional.isEmpty()) {
            return false;
        }

        SmsHistoryEntity sms = optional.get();

        //  urunishlar soni tugadi
        if (sms.getAttemptCount() >= 3) {
            return false;
        }


        if (!code.equals(sms.getCode())) {
            smsSendHistoryRepository.updateAttemptCount(sms.getId());

            return false;
        }


        LocalDateTime expDate = sms.getCreatedAt().plusMinutes(2);
        if (!LocalDateTime.now().isAfter(expDate)) {
            return false;
        }

        return true;
    }

}
