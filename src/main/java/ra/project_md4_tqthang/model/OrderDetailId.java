package ra.project_md4_tqthang.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class OrderDetailId implements Serializable {
    @Column(name = "order_id", insertable = false, updatable = false)
    private Long orderId;
    @Column(name = "product_id", insertable = false, updatable = false)
    private Long productId;
}
