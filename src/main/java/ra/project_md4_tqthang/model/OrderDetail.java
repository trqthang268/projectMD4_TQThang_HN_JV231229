package ra.project_md4_tqthang.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "order_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetail {
    @EmbeddedId
    private OrderDetailId orderDetailId;

    @Column(length = 100)
    private String name;

    @Column(nullable = false)
    private Double unitPrice;

    @Column(nullable = false)
    @Min(0)
    private Integer orderQuantity;
}
