package api.web_blogging.uz.dto.profile;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileDetailUpdatePhotoDTO {
    @NotBlank(message = "Attach id is required")
    private String attachId;

}
