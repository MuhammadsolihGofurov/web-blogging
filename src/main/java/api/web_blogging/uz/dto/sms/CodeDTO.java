package api.web_blogging.uz.dto.sms;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CodeDTO {
    @NotBlank(message = "Code is required")
    private String code;
}
