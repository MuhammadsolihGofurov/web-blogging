package api.web_blogging.uz.dto;

import api.web_blogging.uz.entity.PostEntity;
import lombok.Data;

import java.util.List;

@Data
public class FilterResultDTO<T> {
    private List<T> list;
    private Long totalCount;

    public FilterResultDTO(List<T> list, Long totalCount) {
        this.list = list;
        this.totalCount = totalCount;
    }
}
