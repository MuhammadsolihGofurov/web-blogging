package api.web_blogging.uz.services;

import api.web_blogging.uz.dto.sms.SmsAuthDto;
import api.web_blogging.uz.dto.sms.SmsAuthResponseDto;
import api.web_blogging.uz.dto.sms.SmsRequestDto;
import api.web_blogging.uz.dto.sms.SmsSendResponseDto;
import api.web_blogging.uz.entity.SmsProviderTokenHolider;
import api.web_blogging.uz.enums.SmsType;
import api.web_blogging.uz.exps.AppBadException;
import api.web_blogging.uz.repository.SmsProviderTokenHoliderRepository;
import api.web_blogging.uz.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SmsSendService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${eskiz.url}")
    private String smsUrl;

    @Value("${eskiz.loginUsername}")
    private String loginUsername;

    @Value("${eskiz.loginPassword}")
    private String loginPassword;

    private Integer smsLimit = 3;


    @Autowired
    private SmsProviderTokenHoliderRepository smsProviderTokenHoliderRepository;

    @Autowired
    private SmsSendHistoryService smsSendHistoryService;

    public void sendRegistrationMessage(String phoneNumber) {
        String code = RandomUtil.getRandomSmsCode();
        String message = "Xabar yoziladi: %s";
        message = String.format(message, code);
        sendSmsWithSaveDb(phoneNumber, message, code, SmsType.REGISTRATION);
    }

    public SmsAuthResponseDto sendSmsWithSaveDb(String phoneNumber, String message, String code, SmsType smsType) {
        //  check
        Long count = smsSendHistoryService.getSmsCount(phoneNumber);

        if (count >= smsLimit) {
            throw new AppBadException("Sms limit reached");
        }


        SmsAuthResponseDto result = sendSms(phoneNumber, message, smsType);
        smsSendHistoryService.create(phoneNumber, message, code, smsType);
        return result;
    }

    private SmsAuthResponseDto sendSms(String phoneNumber, String message, SmsType smsType) {
        String token = getToken();
        // send sms
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        SmsRequestDto smsRequest = new SmsRequestDto();
        smsRequest.setMobile_phone(phoneNumber);
        smsRequest.setMessage(message);
        smsRequest.setFrom("4546");

        HttpEntity<SmsRequestDto> request = new HttpEntity<>(smsRequest, headers);

        try {
            String url = smsUrl + "/message/sms/send";
            ResponseEntity<SmsAuthResponseDto> reponse = restTemplate.exchange(url, HttpMethod.POST, request, SmsAuthResponseDto.class);
            // System.out.println(post.getBody());

            return reponse.getBody();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    private String getToken() {
        Optional<SmsProviderTokenHolider> optional = smsProviderTokenHoliderRepository.findTop1();
        if (optional.isEmpty()) {
            String token = getTokenFromProvider();
            SmsProviderTokenHolider sms = new SmsProviderTokenHolider();
            sms.setToken(token);
            sms.setCreatedAt(LocalDateTime.now());
            smsProviderTokenHoliderRepository.save(sms);
            return token;
        }
        SmsProviderTokenHolider sms = optional.get();
        LocalDateTime expDate = sms.getCreatedAt().plusMonths(1);
        if (!LocalDateTime.now().isBefore(expDate)) {
            return sms.getToken();
        }

        // update token
        String token = getTokenFromProvider();
        sms.setToken(token);
        sms.setCreatedAt(LocalDateTime.now());
        smsProviderTokenHoliderRepository.save(sms);
        return "string";
    }


    private String getTokenFromProvider() {
        SmsAuthDto authDto = new SmsAuthDto();
        authDto.setEmail(loginUsername);
        authDto.setPassword(loginPassword);

        try {
            SmsAuthResponseDto response = restTemplate.postForObject(smsUrl + "/auth/login", authDto, SmsAuthResponseDto.class);
            System.out.println(response.getData().getToken());
            return response.getData().getToken();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
