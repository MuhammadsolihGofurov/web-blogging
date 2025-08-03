package api.web_blogging.uz.dto.post;

import api.web_blogging.uz.dto.attach.AttachDTO;
import api.web_blogging.uz.entity.AttachEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {
    private String id;
    private String title;
    private String content;
    private AttachDTO attach;
    private LocalDateTime createdAt;
    private Boolean visible;
}
