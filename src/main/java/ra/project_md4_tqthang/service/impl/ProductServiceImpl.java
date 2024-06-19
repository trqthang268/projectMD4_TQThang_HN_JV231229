package ra.project_md4_tqthang.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ra.project_md4_tqthang.dto.request.ProductRequest;
import ra.project_md4_tqthang.model.Products;
import ra.project_md4_tqthang.repository.ICategoryRepository;
import ra.project_md4_tqthang.repository.IProductRepository;
import ra.project_md4_tqthang.service.IProductService;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service

public class ProductServiceImpl implements IProductService {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public Products getProductInfo(Long productId) {
        return productRepository.findProductsByProductId(productId);
    }

    @Override
    public List<Products> getAllProductsByCategory(Long categoryId) {
        return productRepository.getProductsByCategoryCategoryId(categoryId);
    }

    @Override
    public List<Products> getNewProducts() {
        return productRepository.getNewProducts();
    }

    @Override
    public Page<Products> getAllProducts(Integer page, Integer pageItem, String orderBy, String orderDirection) {
        Pageable pageable = null;
        if (orderBy != null && !orderBy.isEmpty()) {
            //co sap xep
            Sort sort = switch (orderDirection) {
                case "asc" -> Sort.by(orderBy).ascending();
                case "desc" -> Sort.by(orderBy).descending();
                default -> null;
            };
            assert sort != null;
            pageable = PageRequest.of(page, pageItem, sort);
        }else {
            // khong sap xep
            pageable = PageRequest.of(page, pageItem);
        }
        return productRepository.getAllProducts(pageable);
    }

    @Override
    public List<Products> searchProductsByNameOrDesc(String nameOrDesc) {
        return productRepository.searchProductsByNameOrDesc(nameOrDesc);
    }

    @Override
    public List<Products> getBestSellerProduct() {
        return productRepository.getBestSellerProducts();
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public Products updateProduct(ProductRequest productRequest, Long productId) {
        Products products = productRepository.findById(productId)
                .orElseThrow(()-> new NoSuchElementException("Product not found"));
        products.setProductName(productRequest.getProductName());
        products.setDescription(productRequest.getDescription());
        products.setUnitPrice(productRequest.getUnitPrice());
        products.setStockQuantity(productRequest.getStockQuantity());
        products.setCategory(categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(()-> new NoSuchElementException("Category not found")));
        products.setImage(productRequest.getImage());
        products.setUpdatedAt(new Date());
        return productRepository.save(products);
    }

    @Override
    public Products addProduct(Products product) {
        return productRepository.save(product);
    }
}
