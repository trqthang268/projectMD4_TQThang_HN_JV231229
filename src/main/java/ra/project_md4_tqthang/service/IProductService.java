package ra.project_md4_tqthang.service;

import org.springframework.data.domain.Page;
import ra.project_md4_tqthang.model.Products;

import java.util.List;

public interface IProductService {
    Products getProductInfo(Long productId);
    List<Products> getAllProductsByCategory(Long categoryId);
    List<Products> getNewProducts();
    Page<Products> getAllProducts(Integer page, Integer pageItem, String orderBy, String orderDirection);
    List<Products> searchProductsByNameOrDesc(String nameOrDesc);
    List<Products> getBestSellerProduct();
    void deleteProduct(Long productId);
    Products updateProduct(Products product);
    Products addProduct(Products product);
}
