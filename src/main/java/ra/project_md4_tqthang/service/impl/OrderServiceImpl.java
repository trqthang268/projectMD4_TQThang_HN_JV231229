package ra.project_md4_tqthang.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.project_md4_tqthang.constants.OrderStatus;
import ra.project_md4_tqthang.dto.request.CheckoutRequest;
import ra.project_md4_tqthang.exception.CustomException;
import ra.project_md4_tqthang.model.Order;
import ra.project_md4_tqthang.model.OrderDetail;
import ra.project_md4_tqthang.model.ShoppingCart;
import ra.project_md4_tqthang.model.Users;
import ra.project_md4_tqthang.repository.ICartRepository;
import ra.project_md4_tqthang.repository.IUserRepository;
import ra.project_md4_tqthang.repository.OrderDetailRepository;
import ra.project_md4_tqthang.repository.OrderRepository;
import ra.project_md4_tqthang.service.IOrderService;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ICartRepository cartRepository;
    @Autowired
    private IUserRepository userRepository;

    @Override
    public void checkout(Long userId, CheckoutRequest checkoutRequest) throws CustomException {
        Users user = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("user not found"));
        List<ShoppingCart> cartList = cartRepository.findByUserId(user.getUserId());
        if (cartList.isEmpty()) {
            throw new CustomException("Shopping cart is empty", HttpStatus.BAD_REQUEST);
        }
        Double totalPrice = cartList.stream().mapToDouble(
                item->item.getProduct().getUnitPrice()*item.getOrderQuantity()
        ).sum();
        Order order = new Order();
        order.setSerialNumber(UUID.randomUUID().toString());
         order.setUser(user);
         order.setTotalPrice(totalPrice);
         order.setOrderStatus(OrderStatus.WAITING);
         order.setReceiveName(checkoutRequest.getReceiveName());
         order.setReceivePhone(checkoutRequest.getReceiveAddress());
         order.setReceivePhone(checkoutRequest.getReceivePhone());
         order.setCreatedAt(new Date());
         order.setReceivedAt(new Date(System.currentTimeMillis()+(4*24*60*60*1000))); // 4 ngay sau

        orderRepository.save(order);
        List<OrderDetail> orderDetails = cartList.stream().map(
                cartItem->{
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(order);
                    orderDetail.setProducts(cartItem.getProduct());
                    orderDetail.setName(cartItem.getProduct().getProductName());
                    orderDetail.setUnitPrice(cartItem.getProduct().getUnitPrice());
                    orderDetail.setOrderQuantity(cartItem.getOrderQuantity());
                    return orderDetail;
                }
        ).toList();
        orderDetailRepository.saveAll(orderDetails);
        cartRepository.deleteByUser(user); // xoa cart sau khi checkout
    }
}
