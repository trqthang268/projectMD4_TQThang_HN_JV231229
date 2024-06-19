package ra.project_md4_tqthang.model;

import jakarta.persistence.*;
import lombok.*;
import ra.project_md4_tqthang.validation.CategoryNameExist;

@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @CategoryNameExist
    @Column(nullable = false, length = 100)
    private String categoryName;

    private String description;

    @Column(nullable = false)
    private Boolean status;
}
