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
import ra.project_md4_tqthang.constants.EHttpStatus;
import ra.project_md4_tqthang.constants.OrderStatus;
import ra.project_md4_tqthang.dto.request.*;
import ra.project_md4_tqthang.dto.response.ResponseWrapper;
import ra.project_md4_tqthang.exception.CustomException;
import ra.project_md4_tqthang.model.*;
import ra.project_md4_tqthang.repository.IUserRepository;
import ra.project_md4_tqthang.security.principal.UserDetailCustom;
import ra.project_md4_tqthang.service.ICartService;
import ra.project_md4_tqthang.service.IOrderService;
import ra.project_md4_tqthang.service.impl.AddressServiceImpl;
import ra.project_md4_tqthang.service.impl.UserServiceImpl;
import ra.project_md4_tqthang.service.impl.WishlistServiceImpl;

import java.util.List;
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
    @Autowired
    private AddressServiceImpl addressService;
    @Autowired
    private WishlistServiceImpl wishlistService;


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
        ShoppingCart shoppingCart = cartService.addToCart(users.getUserId(), request);
        return new ResponseEntity<>(shoppingCart,HttpStatus.OK);
    }

    //ROLE_USER - PUT - Thay đổi số lượng đặt hàng của 1 sản phẩm (payload :quantity) #4882
    @PutMapping("/cart/items/{cartItemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long cartItemId, @RequestBody UpdateCartQuantityRequest request) {
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userDetailCustom.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        ShoppingCart cartItem = cartService.getCartById(cartItemId);
        if (!cartItem.getUser().getUserId().equals(users.getUserId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(cartService.updateCartItemQuantity(cartItemId, request.getOrderQuantity()),HttpStatus.OK);
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
        return new ResponseEntity<>(userService.updateUser(users.getUserId(), updateUserRequest),HttpStatus.OK);
    }

    //ROLE_USER - PUT - Thay đổi mật khẩu (payload : oldPass, newPass, confirmNewPass) #4888
    @PutMapping("/account/change-password")
    public   ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) throws CustomException {
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(userDetailCustom.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new ResponseEntity<>(ResponseWrapper.builder()
                .data(userService.changePassword(users.getUserId(), request))
                .eHttpStatus(EHttpStatus.SUCCESS)
                .build(),HttpStatus.OK);

    }

    //ROLE_USER - POST - thêm mới địa chỉ #4889
    @PostMapping("/account/addresses")
    public ResponseEntity<?> addAddress(@RequestBody NewAddressRequest newAddressRequest){
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(userDetailCustom.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new ResponseEntity<>(addressService.addAddress(users.getUserId(), newAddressRequest),HttpStatus.CREATED);
    }

    //ROLE_USER - DELETE - Xóa 1 địa chỉ theo mã địa chỉ #4890
    @DeleteMapping("/account/addresses/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long addressId){
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(userDetailCustom.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        addressService.deleteAddress(users.getUserId(), addressId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //ROLE_USER - GET - lấy ra danh sách địa chỉ của người dùng #4891
    @GetMapping("/account/addresses")
    public ResponseEntity<List<Address>> getAddresses(){
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(userDetailCustom.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new ResponseEntity<>(addressService.getAddressList(users.getUserId()), HttpStatus.OK);
    }

    //ROLE_USER - GET - lấy địa chỉ người dùng theo addressId  #4892
    @GetMapping("/account/addresses/{addressId}")
    public ResponseEntity<Address> getAddress(@PathVariable Long addressId){
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(userDetailCustom.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        Address address = addressService.getAddress(users.getUserId(), addressId);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    //ROLE_USER - GET - lấy ra danh sách lịch sử mua hàng #4893
    @GetMapping("/history")
    public ResponseEntity<List<Order>> getHistory(){
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(userDetailCustom.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new ResponseEntity<>(orderService.findByUserId(users.getUserId()), HttpStatus.OK);
    }

    //ROLE_USER - GET - lấy ra chi tiết đơn hàng theo số serial  #4894
    @GetMapping("/history/{serialNumber}")
    public ResponseEntity<Order> getHistory(@PathVariable String serialNumber){
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(userDetailCustom.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new ResponseEntity<>(orderService.findBySerialNumber(users.getUserId(),serialNumber), HttpStatus.OK);
    }

    //ROLE_USER - GET - lấy ra danh sách lịch sử đơn hàng theo trạng thái đơn hàng #4895
    @GetMapping("/history/{orderStatus}")
    public ResponseEntity<List<Order>> getUserOrderHistoryByStatus(@PathVariable OrderStatus orderStatus){
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(userDetailCustom.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        List<Order> orderList = orderService.getUserOrderHistoryByStatus(users.getUserId(), orderStatus);
        return new ResponseEntity<>(orderList,HttpStatus.OK);
    }

    //ROLE_USER - PUT - Hủy đơn hàng đang trong trạng thái chờ xác nhận  #4896
    @PutMapping("/history/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) throws CustomException {
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(userDetailCustom.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return new ResponseEntity<>(orderService.cancelOrder(users.getUserId(),orderId),HttpStatus.OK);
    }

    //ROLE_USER - POST-Thêm mới 1 sản phẩm vào danh sách yêu thích (payload : productId ) #4897
    @PostMapping("/wish-list")
    public ResponseEntity<?> addToWishlist(@RequestBody WishlistRequest wishlistRequest) throws CustomException {
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(userDetailCustom.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new ResponseEntity<>(wishlistService.addToWishlist(users.getUserId(), wishlistRequest), HttpStatus.CREATED);
    }

    //ROLE_USER - GET - Lấy ra danh sách yêu thích #4898
    @GetMapping("/wish-list")
    public ResponseEntity<List<WishList>> getWishlistByUser(){
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(userDetailCustom.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        List<WishList> wishLists = wishlistService.getUserWishlist(users.getUserId());
        return new ResponseEntity<>(wishLists,HttpStatus.OK);
    }

    //ROLE_USER - DELETE - Xóa sản phẩm ra khỏi danh sách yêu thích  #4899
    @DeleteMapping("/wish-list/{wishListId}")
    public ResponseEntity<?> deleteWishlist(@PathVariable Long wishListId){
        UserDetailCustom userDetailCustom = (UserDetailCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findById(userDetailCustom.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        wishlistService.removeFromWishList(users.getUserId(),wishListId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
