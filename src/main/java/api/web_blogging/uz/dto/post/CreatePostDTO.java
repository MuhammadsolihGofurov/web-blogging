package api.web_blogging.uz.dto.post;

import api.web_blogging.uz.dto.attach.AttachCreateDTO;
import api.web_blogging.uz.entity.AttachEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
public class CreatePostDTO {
    @NotBlank(message = "Title is required")
    @Length(min = 5, max = 255, message = "min-5, max-255")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Photo is required")
    private AttachCreateDTO attach;
}
