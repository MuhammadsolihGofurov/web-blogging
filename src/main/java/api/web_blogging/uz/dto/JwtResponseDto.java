package api.web_blogging.uz.dto;

import api.web_blogging.uz.enums.ProfileRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponseDto {
    private Integer id;
    private String username;
    private List<ProfileRole> roles;
}
