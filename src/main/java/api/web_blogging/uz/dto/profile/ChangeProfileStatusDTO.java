package api.web_blogging.uz.dto.profile;

import api.web_blogging.uz.enums.GeneralStatus;
import lombok.Data;

@Data
public class ChangeProfileStatusDTO {

    private GeneralStatus status;

}
