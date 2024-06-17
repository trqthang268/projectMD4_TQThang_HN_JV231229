package ra.project_md4_tqthang.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false, unique = true, length = 100)
    private String sku = UUID.randomUUID().toString();

    @Column(nullable = false, length = 100)
    private String productName;

    private String description;

    @Column(nullable = false)
    private Double unitPrice;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(length = 255)
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false,referencedColumnName = "category_id")
    private Category category;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date updatedAt;


}
