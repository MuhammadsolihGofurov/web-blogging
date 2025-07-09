package api.web_blogging.uz.dto.sms;


import lombok.Data;

@Data
public class SmsRequestDto {
    private String mobile_phone;
    private String message;
    private String from;
}
