package ra.project_md4_tqthang.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PagingRequest {
    private String searchName;
    private Integer page;
    private Integer pageItem;
    private String sortBy;
    private String orderDirection;
}
