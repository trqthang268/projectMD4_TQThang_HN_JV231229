package ra.project_md4_tqthang.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shopping_cart")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shoppingcart_id")
    private Long shoppingCartId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false,referencedColumnName = "product_id")
    private Products product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false,referencedColumnName = "user_id")
    private Users user;

    @Column(nullable = false)
    private Integer orderQuantity;
}
