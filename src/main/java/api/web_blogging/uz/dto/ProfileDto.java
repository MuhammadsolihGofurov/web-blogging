package api.web_blogging.uz.dto;


import api.web_blogging.uz.enums.GeneralStatus;
import api.web_blogging.uz.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDto {
    private String name;
    private String username;
    private List<ProfileRole> roles;
    private String token;
    private String photoUrl;
    private GeneralStatus status;
}
