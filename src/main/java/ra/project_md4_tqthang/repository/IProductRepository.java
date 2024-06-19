package ra.project_md4_tqthang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ra.project_md4_tqthang.model.Products;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<Products, Long> , PagingAndSortingRepository<Products, Long> {
    //get theo p_id
    @Query("select p from Products p where p.productId = :productId")
    Products findProductsByProductId(Long productId);
    // get theo cateId
    List<Products> getProductsByCategoryCategoryId(Long categoryId);
    // get san pham moi
    @Query("select p from Products p order by p.createdAt desc")
    List<Products> getNewProducts();
    //get all product phan trang sap xep
    @Query("select p from Products p")
    Page<Products> getAllProducts(Pageable pageable);
    //get all by name or desc
    @Query("select p from Products p where p.productName like concat('%',:productNameOrDesc,'%') or p.description like concat('%',:productNameOrDesc,'%')")
    List<Products> searchProductsByNameOrDesc(String productNameOrDesc);
    // get best seller
    @Query("select p from Products p join OrderDetail od on p.productId=od.products.productId group by p.productId order by sum(od.orderQuantity) desc")
    List<Products> getBestSellerProducts();

    Optional<Products> findProductsByProductName(String productName);
}
