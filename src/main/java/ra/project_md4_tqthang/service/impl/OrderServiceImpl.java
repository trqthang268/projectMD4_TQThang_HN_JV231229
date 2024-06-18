package ra.project_md4_tqthang.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.project_md4_tqthang.constants.EHttpStatus;
import ra.project_md4_tqthang.constants.OrderStatus;
import ra.project_md4_tqthang.dto.request.CheckoutRequest;
import ra.project_md4_tqthang.dto.request.OrderStatusRequest;
import ra.project_md4_tqthang.exception.CustomException;
import ra.project_md4_tqthang.model.*;
import ra.project_md4_tqthang.repository.*;
import ra.project_md4_tqthang.service.IOrderService;
import java.util.*;

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
    @Autowired
    private IProductRepository productRepository;

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
//        List<OrderDetail> orderDetails = cartList.stream().map(
//                cartItem->{
//                    OrderDetail orderDetail = new OrderDetail();
//                    orderDetail.setOrderDetailId(new OrderDetailId(order.getOrderId(),cartItem.getProduct().getProductId()));
//                    orderDetail.setOrder(order);
//                    orderDetail.setProducts(cartItem.getProduct());
//                    orderDetail.setName(cartItem.getProduct().getProductName());
//                    orderDetail.setUnitPrice(cartItem.getProduct().getUnitPrice());
//                    orderDetail.setOrderQuantity(cartItem.getOrderQuantity());
//
//                    return orderDetail;
//                }
//        ).toList();
//        orderDetailRepository.saveAll(orderDetails);
        for (ShoppingCart shoppingCart : cartList){
            Products products =productRepository.findById(shoppingCart.getProduct().getProductId())
                    .orElseThrow(()->new NoSuchElementException("product not found"));
            OrderDetail orderDetail = OrderDetail.builder()
                    .orderDetailId(new OrderDetailId(order.getOrderId(),products.getProductId()))
                    .order(order)
                    .products(products)
                    .orderQuantity(shoppingCart.getOrderQuantity())
                    .name(products.getProductName())
                    .unitPrice(products.getUnitPrice())
                    .build();
            if (products.getStockQuantity()<orderDetail.getOrderQuantity()){
                throw new CustomException("Product has no stock",HttpStatus.BAD_REQUEST);
            }
            products.setStockQuantity(products.getStockQuantity() - orderDetail.getOrderQuantity());
            orderDetailRepository.save(orderDetail);
            productRepository.save(products);
        }
        cartRepository.deleteByUser(user); // xoa cart sau khi checkout
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrderByStatus(OrderStatus status) {
        return orderRepository.getOrdersByOrderStatus(status);
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
        return orderDetailRepository.findAllByOrder_OrderId(orderId);
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatusRequest statusRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new NoSuchElementException("order not found"));
        order.setOrderStatus(statusRequest.getOrderStatus());
        orderRepository.save(order);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("user not found"));
        List<Order> orderList = orderRepository.findOrderByUser(users);
        List<Order> orderListExcludingtWait = new ArrayList<>();
        for (Order order : orderList) {
            if (order.getOrderStatus() != OrderStatus.WAITING) {
                orderListExcludingtWait.add(order);
            }
        }
        return orderListExcludingtWait;
    }

    @Override
    public Order findBySerialNumber(Long userId, String serialNumber) {
        Users users = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("user not found"));
        return orderRepository.findOrderBySerialNumberAndUser(serialNumber,users)
                .orElseThrow(()->new NoSuchElementException("order not found"));
    }

    @Override
    public List<Order> getUserOrderHistoryByStatus(Long userId, OrderStatus status) {
        Users users = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("user not found"));
        return orderRepository.findByUserAndOrderStatus(users,status);
    }

    @Override
    public void cancelOrder(Long userId, Long orderId) throws CustomException {
        Users users = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("user not found"));
        Order order = orderRepository.findByOrderIdAndUser(orderId,users)
                .orElseThrow(()->new NoSuchElementException("order not found"));
        if (order.getOrderStatus()!=OrderStatus.WAITING){
            throw new CustomException("Order cannot be canceled as it is not in WAITING", HttpStatus.BAD_REQUEST);
        }
        order.setOrderStatus(OrderStatus.CANCEL);
        orderRepository.save(order);
        //tra lai stock
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrder_OrderId(orderId);
        for (OrderDetail detail : orderDetails) {
            Products products = detail.getProducts();
            products.setStockQuantity(products.getStockQuantity()+detail.getOrderQuantity());
            productRepository.save(products);
        }
    }
}
