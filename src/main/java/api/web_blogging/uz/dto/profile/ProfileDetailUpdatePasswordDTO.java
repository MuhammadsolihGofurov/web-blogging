package api.web_blogging.uz.dto.profile;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileDetailUpdatePasswordDTO {
    @NotBlank(message = "Current Password is required")
    private String currentPassword;

    @NotBlank(message = "New Password is required")
    private String newPassword;

}
