package api.web_blogging.uz.dto.sms;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SmsResendDTO {
    @NotBlank(message = "Phone is required")
    private String phone;
}
