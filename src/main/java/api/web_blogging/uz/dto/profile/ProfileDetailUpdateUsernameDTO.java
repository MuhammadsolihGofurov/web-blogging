package api.web_blogging.uz.dto.profile;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileDetailUpdateUsernameDTO {
    @NotBlank(message = "Username is required")
    private String username;
}
