package ra.project_md4_tqthang.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.project_md4_tqthang.validation.ProductNameExist;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductRequest {
    private Long productId;
    @NotBlank(message = "Product name is blank")
    @NotEmpty(message = "Product name is empty")
    @ProductNameExist
    private String productName;
    private String description;
    private Double unitPrice;
    @Min(value = 1,message = "Stock must greater than 1")
    private Integer stockQuantity;
    private String image="https://gebelesebeti.ge/front/asset/img/default-product.png";
    private Long categoryId;
}
