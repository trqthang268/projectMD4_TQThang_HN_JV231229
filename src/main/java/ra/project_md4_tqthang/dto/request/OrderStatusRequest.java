package ra.project_md4_tqthang.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ra.project_md4_tqthang.constants.OrderStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderStatusRequest {
    private OrderStatus orderStatus;
}
