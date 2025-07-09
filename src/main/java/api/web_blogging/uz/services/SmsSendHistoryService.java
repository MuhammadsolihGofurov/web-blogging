package api.web_blogging.uz.services;


import api.web_blogging.uz.entity.SmsHistoryEntity;
import api.web_blogging.uz.enums.SmsType;
import api.web_blogging.uz.repository.SmsSendHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SmsSendHistoryService {
    @Autowired
    private SmsSendHistoryRepository smsSendHistoryRepository;


    public void create(String phone, String message, String code, SmsType smsType) {
        SmsHistoryEntity sms = new SmsHistoryEntity();
        sms.setPhone(phone);
        sms.setMessage(message);
        sms.setCode(code);
        sms.setSmsType(smsType);
        sms.setCreatedAt(LocalDateTime.now());

        smsSendHistoryRepository.save(sms);
    }


    public Long getSmsCount(String phoneNumber) {
        LocalDateTime now = LocalDateTime.now();
        return smsSendHistoryRepository.countByPhoneAndCreatedAtBefore(phoneNumber, now.minusMinutes(1), now);
    }

}
