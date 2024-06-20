package ra.project_md4_tqthang.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private Users user;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(length = 15, name = "phone")
    private String phone;

    @Column(length = 50,name = "receive_name")
    private String receiveName;
}
