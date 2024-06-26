package ra.project_md4_tqthang.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
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
         order.setNote(checkoutRequest.getNote());
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
                    .orderDetailId(new OrderDetailId(order,products))
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
        cartRepository.deleteShoppingCartByUser_UserId(user.getUserId()); // xoa cart sau khi checkout
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrderByStatus(String status) {
        return switch (status.toUpperCase().trim()){
            case "WAITING" -> orderRepository.getOrdersByOrderStatus(OrderStatus.WAITING);
            case "CONFIRM" -> orderRepository.getOrdersByOrderStatus(OrderStatus.CONFIRM);
            case "DELIVERY" -> orderRepository.getOrdersByOrderStatus(OrderStatus.DELIVERY);
            case "DENIED" -> orderRepository.getOrdersByOrderStatus(OrderStatus.DENIED);
            case "CANCEL" -> orderRepository.getOrdersByOrderStatus(OrderStatus.CANCEL);
            case "SUCCESS" -> orderRepository.getOrdersByOrderStatus(OrderStatus.SUCCESS);
            default -> throw new IllegalStateException("Unexpected value: " + status.toUpperCase().trim());
        }
                ;
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
        return orderDetailRepository.findAllByOrder_OrderId(orderId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, String statusRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new NoSuchElementException("order not found"));
        switch (statusRequest.toUpperCase().trim()){
            case "WAITING" -> order.setOrderStatus(OrderStatus.WAITING);
            case "CONFIRM" -> order.setOrderStatus(OrderStatus.CONFIRM);
            case "DELIVERY" -> order.setOrderStatus(OrderStatus.DELIVERY);
            case "DENIED" -> order.setOrderStatus(OrderStatus.DENIED);
//            case "CANCEL" -> order.setOrderStatus(OrderStatus.CANCEL);
            case "SUCCESS" -> order.setOrderStatus(OrderStatus.SUCCESS);
            default -> throw new NoSuchElementException("Status not valid");
        }
        return orderRepository.save(order);
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
    public Order cancelOrder(Long userId, Long orderId) throws CustomException {
        Users users = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("user not found"));
        Order order = orderRepository.findByOrderIdAndUser(orderId,users)
                .orElseThrow(()->new NoSuchElementException("order not found"));
        if (order.getOrderStatus()!=OrderStatus.WAITING){
            throw new CustomException("Order cannot be canceled as it is not in WAITING", HttpStatus.BAD_REQUEST);
        }
        //tra lai stock
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrder_OrderId(orderId);
        for (OrderDetail detail : orderDetails) {
            Products products = productRepository.findById(detail.getOrderDetailId().getProducts().getProductId()).orElseThrow(()->new NoSuchElementException("product not found"));
            products.setStockQuantity(products.getStockQuantity()+detail.getOrderQuantity());
            productRepository.save(products);
        }
        order.setOrderStatus(OrderStatus.CANCEL);
        return orderRepository.save(order);
    }
}
