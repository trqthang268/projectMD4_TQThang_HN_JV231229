package ra.project_md4_tqthang.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.project_md4_tqthang.dto.request.WishlistRequest;
import ra.project_md4_tqthang.exception.CustomException;
import ra.project_md4_tqthang.model.Products;
import ra.project_md4_tqthang.model.Users;
import ra.project_md4_tqthang.model.WishList;
import ra.project_md4_tqthang.repository.IProductRepository;
import ra.project_md4_tqthang.repository.IUserRepository;
import ra.project_md4_tqthang.repository.WishlistRepository;
import ra.project_md4_tqthang.service.IWishlistService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WishlistServiceImpl implements IWishlistService {
    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IProductRepository productRepository;

    @Override
    public WishList addToWishlist(Long userId, WishlistRequest wishlistRequest) throws CustomException {
        Users users = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("user not found"));
        Products products = productRepository.findById(wishlistRequest.getProductId())
                .orElseThrow(()->new NoSuchElementException("product not found"));

        if (wishlistRepository.findByUserAndProduct(users,products).isPresent()){
            throw new CustomException("Product is already in the wishlist", HttpStatus.BAD_REQUEST);
        }
        WishList newWishList = new WishList();
        newWishList.setProduct(products);
        newWishList.setUser(users);
        return wishlistRepository.save(newWishList);
    }

    @Override
    public List<WishList> getUserWishlist(Long userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("user not found"));
        return wishlistRepository.findByUser(users);
    }

    @Override
    public void removeFromWishList(Long userId, Long wishlistId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("user not found"));
        WishList wishList = wishlistRepository.findByWishListIdAndUser(wishlistId,users)
                .orElseThrow(()->new NoSuchElementException("wishlist item not found or does not belong to the user"));
        wishlistRepository.delete(wishList);
    }
}
