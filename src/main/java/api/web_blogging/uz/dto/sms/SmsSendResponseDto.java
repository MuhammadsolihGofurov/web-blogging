package api.web_blogging.uz.dto.sms;

import lombok.Data;

@Data
public class SmsSendResponseDto {
    private String id;
    private String message;
    private String status;
}
