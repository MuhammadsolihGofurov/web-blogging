package api.web_blogging.uz.dto;


import api.web_blogging.uz.enums.ProfileRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ProfileDto {
    private String name;
    private String username;
    private List<ProfileRole> roles;
    private String token;
}
