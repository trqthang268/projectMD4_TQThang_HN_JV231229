package ra.project_md4_tqthang.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_md4_tqthang.dto.request.PagingRequest;
import ra.project_md4_tqthang.model.Products;
import ra.project_md4_tqthang.service.IProductService;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    //PermitAll - GET - Chi tiết thông tin sản phẩm theo id #4879
    @GetMapping("/{productId}")
    public ResponseEntity<Products> getProduct(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.getProductInfo(productId), HttpStatus.OK);
    }

    //PermitAll - GET - Danh sách sản phẩm theo danh mục #4878
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<Products>> getProductByCategory(@PathVariable Long categoryId) {
        List<Products> products = productService.getAllProductsByCategory(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //PermitAll - GET - Danh sách sản phẩm mới #4876
    @GetMapping("/new-products")
    public ResponseEntity<List<Products>> getNewProducts() {
        List<Products> products = productService.getNewProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //PermitAll - GET - Danh sách sản phẩm được bán(có phân trang và sắp xếp) #4874
    @GetMapping
    public ResponseEntity<List<Products>> getAllProducts(@RequestBody PagingRequest pagingRequest) {
        List<Products> products = productService.getAllProducts(
                pagingRequest.getPage()-1,
                pagingRequest.getPageItem(),
                pagingRequest.getSortBy(),
                pagingRequest.getOrderDirection()
        ).getContent();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //PermitAll - GET - Tìm kiếm sản phẩm theo tên hoặc mô tả #4873
    @GetMapping("/search")
    public ResponseEntity<List<Products>> getProductsByNameOrDescription(@RequestParam("nameOrDescription") String nameOrDescription) {
        List<Products> products = productService.searchProductsByNameOrDesc(nameOrDescription);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //PermitAll - GET - Danh sách sản phẩm bán chạy #4877
    @GetMapping("/best-seller-products")
    public ResponseEntity<List<Products>> getBestSellerProducts() {
        List<Products> products = productService.getBestSellerProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    //PermitAll - GET - Danh sách sản phẩm nổi bật - Không bắt buộc #4875

}
