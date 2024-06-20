package ra.project_md4_tqthang.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.project_md4_tqthang.dto.request.AddToCartRequest;
import ra.project_md4_tqthang.exception.CustomException;
import ra.project_md4_tqthang.model.Products;
import ra.project_md4_tqthang.model.ShoppingCart;
import ra.project_md4_tqthang.model.Users;
import ra.project_md4_tqthang.repository.ICartRepository;
import ra.project_md4_tqthang.repository.IProductRepository;
import ra.project_md4_tqthang.repository.IUserRepository;
import ra.project_md4_tqthang.service.ICartService;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private ICartRepository cartRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IProductRepository productRepository;

    @Override
    public List<ShoppingCart> getAllShoppingCart(Users users) {
        return cartRepository.findByUserId(users.getUserId());
    }

    @Override
    public ShoppingCart addToCart(Long userId, AddToCartRequest request) {
        Users users = userRepository.findById(userId).orElseThrow(()->new NoSuchElementException("User not found"));
        Products products = productRepository.findById(request.getProductId()).orElseThrow(()->new NoSuchElementException("Product not found"));
        ShoppingCart cart = cartRepository.findByUserAndProduct(users,products).orElse(new ShoppingCart());
        cart.setUser(users);
        cart.setProduct(products);
        if (cart.getOrderQuantity()==null){
            cart.setOrderQuantity(request.getOrderQuantity());
        }else {
            cart.setOrderQuantity(cart.getOrderQuantity()+request.getOrderQuantity());
        }
        return cartRepository.save(cart);
    }

    @Override
    public ShoppingCart updateCartItemQuantity(Long cartId, Integer quantity) {
        ShoppingCart cartItem = cartRepository.findById(cartId).orElseThrow(()->new NoSuchElementException("Cart not found"));
        cartItem.setOrderQuantity(quantity);
        return cartRepository.save(cartItem);
    }

    @Override
    public ShoppingCart getCartById(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow(()->new NoSuchElementException("Cart not found"));
    }

    @Override
    public void deleteCartItem(Long cartId, Long userId) throws CustomException {
        ShoppingCart cartItem = cartRepository.findById(cartId)
                .orElseThrow(()->new NoSuchElementException("Cart not found"));
        if (!cartItem.getUser().getUserId().equals(userId)) {
            throw new CustomException("Unauthorized to delete this cart item", HttpStatus.BAD_REQUEST);
        }
        cartRepository.delete(cartItem);
    }

    @Override
    public void clearCart(Long userId) {
        Users users = userRepository.findById(userId).orElseThrow(()->new NoSuchElementException("User not found"));
        cartRepository.deleteShoppingCartByUser_UserId(users.getUserId());
    }
}
