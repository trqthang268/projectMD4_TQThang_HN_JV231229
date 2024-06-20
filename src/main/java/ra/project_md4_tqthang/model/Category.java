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
    @Column(name = "category_id")
    private Long categoryId;

//    @CategoryNameExist
    @Column(length = 100,name = "category_name")
    private String categoryName;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Boolean status;
}
