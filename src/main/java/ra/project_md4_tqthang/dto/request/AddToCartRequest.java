package ra.project_md4_tqthang.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AddToCartRequest {
    private Long productId;
    private Integer orderQuantity;

}
