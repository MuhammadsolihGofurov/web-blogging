package api.web_blogging.uz.dto.attach;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttachCreateDTO {
    @NotNull(message = "Id is required")
    private String id;
}
