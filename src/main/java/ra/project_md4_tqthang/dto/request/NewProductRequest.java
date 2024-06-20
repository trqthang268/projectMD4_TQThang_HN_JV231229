package ra.project_md4_tqthang.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import ra.project_md4_tqthang.validation.ProductNameExist;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewProductRequest {
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
    private Date createDate;
}
