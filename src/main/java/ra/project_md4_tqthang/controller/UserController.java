package ra.project_md4_tqthang.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ra.project_md4_tqthang.dto.request.AddToCartRequest;
import ra.project_md4_tqthang.dto.request.ChangePasswordRequest;
import ra.project_md4_tqthang.dto.request.CheckoutRequest;
import ra.project_md4_tqthang.dto.request.UpdateUserRequest;
import ra.project_md4_tqthang.exception.CustomException;
import ra.project_md4_tqthang.model.ShoppingCart;
import ra.project_md4_tqthang.model.Users;
import ra.project_md4_tqthang.repository.IUserRepository;
import ra.project_md4_tqthang.security.principal.UserDetailCustom;
import ra.project_md4_tqthang.service.ICartService;
import ra.project_md4_tqthang.service.IOrderService;
import ra.project_md4_tqthang.service.impl.UserServiceImpl;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api.myservice.com/v1/user")

public class UserController {
    @Autowired
    private ICartService cartService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private UserServiceImpl userService;


    //ROLE_USER - GET - Danh sách sản phẩm trong giỏ hàng #4880
    @GetMapping("/cart/list")
    public ResponseEntity<?> getCartList( ) {
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userDetailCustom.getUsername())
                .orElseThrow(()->new NoSuchElementException("user not found"));
        return new ResponseEntity<>(cartService.getAllShoppingCart(users), HttpStatus.OK);
    }

    //ROLE_USER - POST - Thêm mới sản phẩm vào giỏ hàng (payload: productId and quantity)  #4881
    @PostMapping("/cart/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request) {
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userDetailCustom.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        cartService.addToCart(users.getUserId(), request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //ROLE_USER - PUT - Thay đổi số lượng đặt hàng của 1 sản phẩm (payload :quantity) #4882
    @PutMapping("/cart/items/{cartItemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long cartItemId, @RequestBody AddToCartRequest request) {
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userDetailCustom.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        ShoppingCart cartItem = cartService.getCartById(cartItemId);
        if (!cartItem.getUser().getUserId().equals(users.getUserId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        cartService.updateCartItemQuantity(cartItemId, request.getOrderQuantity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //ROLE_USER - DELETE - Xóa 1 sản phẩm trong giỏ hàng  #4883
    @DeleteMapping("/cart/items/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId) throws CustomException {
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userDetailCustom.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        cartService.deleteCartItem(cartItemId, users.getUserId());
        return new ResponseEntity<>(ResponseEntity.noContent(), HttpStatus.OK);
    }

    //ROLE_USER - DELETE - Xóa toàn bộ sản phẩm trong giỏ hàng #4884
    @DeleteMapping("/cart/clear")
    public ResponseEntity<?> clearCart() {
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userDetailCustom.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        cartService.clearCart(users.getUserId());
        return new ResponseEntity<>(ResponseEntity.noContent(),HttpStatus.OK);
    }

    //ROLE_USER - POST - Đặt hàng #4885
    @PostMapping("/cart/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request) throws CustomException {
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userDetailCustom.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        orderService.checkout(users.getUserId(),request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //ROLE_USER - GET - Thông tin tài khoản người dùng  #4886
    @GetMapping("/account")
    public ResponseEntity<?> getAccount() {
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userDetailCustom.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //ROLE_USER - PUT - Cập nhật thông tin người dùng  #4887
    @PutMapping("/account")
    public ResponseEntity<?> updateAccount(@RequestBody UpdateUserRequest updateUserRequest) {
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userDetailCustom.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        userService.updateUser(users.getUserId(), updateUserRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //ROLE_USER - PUT - Thay đổi mật khẩu (payload : oldPass, newPass, confirmNewPass) #4888
    @PutMapping("/account/change-password")
    private  ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) throws CustomException {
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(userDetailCustom.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        userService.changePassword(users.getUserId(), request);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
