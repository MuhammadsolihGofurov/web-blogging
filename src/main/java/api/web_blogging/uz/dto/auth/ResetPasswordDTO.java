package api.web_blogging.uz.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordDTO {
    @NotBlank(message = "Username is required")
    private String username;
}
