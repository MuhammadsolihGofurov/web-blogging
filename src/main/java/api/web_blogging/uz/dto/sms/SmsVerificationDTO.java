package api.web_blogging.uz.dto.sms;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SmsVerificationDTO {
    @NotBlank(message = "Phone is required")
    private String phone;
    @NotBlank(message = "Code is required")
    private String code;
}
