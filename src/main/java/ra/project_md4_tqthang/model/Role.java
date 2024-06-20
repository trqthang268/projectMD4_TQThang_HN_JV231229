package ra.project_md4_tqthang.model;

import jakarta.persistence.*;
import lombok.*;
import ra.project_md4_tqthang.constants.RoleName;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}
