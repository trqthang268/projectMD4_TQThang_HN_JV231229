package ra.project_md4_tqthang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.project_md4_tqthang.model.Products;
import ra.project_md4_tqthang.model.ShoppingCart;
import ra.project_md4_tqthang.model.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUserAndProduct(Users user, Products product);
    void deleteByUser(Users user);
    @Query("select c from ShoppingCart c where c.user.userId=:userId")
    List<ShoppingCart> findByUserId(Long userId);

}
