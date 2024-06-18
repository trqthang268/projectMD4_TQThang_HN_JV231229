package ra.project_md4_tqthang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project_md4_tqthang.model.Products;
import ra.project_md4_tqthang.model.Users;
import ra.project_md4_tqthang.model.WishList;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<WishList,Long> {
    Optional<WishList> findByUserAndProduct(Users users, Products products);
    List<WishList>  findByUser(Users users);
    Optional<WishList> findByWishListIdAndUser(Long wishlistId,Users users);
}
