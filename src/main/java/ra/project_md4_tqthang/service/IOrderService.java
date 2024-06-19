package ra.project_md4_tqthang.service;

import ra.project_md4_tqthang.constants.OrderStatus;
import ra.project_md4_tqthang.dto.request.CheckoutRequest;
import ra.project_md4_tqthang.dto.request.OrderStatusRequest;
import ra.project_md4_tqthang.exception.CustomException;
import ra.project_md4_tqthang.model.Order;
import ra.project_md4_tqthang.model.OrderDetail;

import java.util.List;

public interface IOrderService {
    void checkout(Long userId, CheckoutRequest checkoutRequest) throws CustomException;
    List<Order> getAllOrders();
    List<Order> getOrderByStatus(OrderStatus status);
    List<OrderDetail> getOrderDetailsByOrderId(Long orderId);
    Order updateOrderStatus(Long orderId, OrderStatusRequest statusRequest);
    List<Order> findByUserId(Long userId);
    Order findBySerialNumber(Long userId, String serialNumber);
    List<Order> getUserOrderHistoryByStatus(Long userId, OrderStatus status);
    Order cancelOrder(Long userId,Long orderId) throws CustomException;
}
