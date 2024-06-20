package ra.project_md4_tqthang.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ra.project_md4_tqthang.constants.OrderStatus;

import java.util.Date;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(unique = true, length = 100)
    private String serialNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column()
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column()
    private OrderStatus orderStatus;

    @Column(length = 100)
    private String note;

    @Column(length = 100)
    private String receiveName;

    @Column(length = 255)
    private String receiveAddress;

    @Column(length = 15)
    private String receivePhone;

    @Column()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date createdAt ;//= new Date();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date receivedAt;
}
