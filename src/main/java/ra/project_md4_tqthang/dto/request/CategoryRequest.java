package ra.project_md4_tqthang.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.project_md4_tqthang.validation.CategoryNameExist;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryRequest {
    @CategoryNameExist
    @NotEmpty(message = "Category name is empty")
    @NotBlank(message = "Category name is blank")
    private String categoryName;
    private String description;
    private Boolean status;
}
