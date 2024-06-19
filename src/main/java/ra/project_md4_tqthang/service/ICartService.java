package ra.project_md4_tqthang.service;

import ra.project_md4_tqthang.dto.request.AddToCartRequest;
import ra.project_md4_tqthang.exception.CustomException;
import ra.project_md4_tqthang.model.ShoppingCart;
import ra.project_md4_tqthang.model.Users;

import java.util.List;
import java.util.Optional;

public interface ICartService {
    List<ShoppingCart> getAllShoppingCart(Users users);
    ShoppingCart addToCart(Long userId, AddToCartRequest request);
    ShoppingCart updateCartItemQuantity(Long cartId, Integer quantity);
    ShoppingCart getCartById(Long cartId);
    void deleteCartItem(Long cartId,Long userId) throws CustomException;
    void clearCart(Long userId);
}
