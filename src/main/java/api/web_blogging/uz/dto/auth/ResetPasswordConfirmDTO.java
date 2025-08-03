package api.web_blogging.uz.dto.auth;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordConfirmDTO {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "new Password is required")
    private String newPassword;
    @NotBlank(message = "Code is required")
    private String code;

}
