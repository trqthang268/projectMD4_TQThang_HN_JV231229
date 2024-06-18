package ra.project_md4_tqthang.service;

import ra.project_md4_tqthang.dto.request.WishlistRequest;
import ra.project_md4_tqthang.exception.CustomException;
import ra.project_md4_tqthang.model.WishList;

import java.util.List;

public interface IWishlistService {
    void addToWishlist(Long userId, WishlistRequest wishlistRequest) throws CustomException;
    List<WishList> getUserWishlist(Long userId);
    void removeFromWishList(Long userId,Long wishlistId);
}
