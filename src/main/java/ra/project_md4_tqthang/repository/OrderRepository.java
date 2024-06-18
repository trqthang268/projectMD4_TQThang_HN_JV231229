package ra.project_md4_tqthang.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project_md4_tqthang.constants.OrderStatus;
import ra.project_md4_tqthang.model.Order;
import ra.project_md4_tqthang.model.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> getOrdersByOrderStatus(OrderStatus orderStatus);
    List<Order> findOrderByUser(Users user);
    Optional<Order> findOrderBySerialNumberAndUser(String serialNumber,Users user);
    List<Order> findByUserAndOrderStatus(Users user,OrderStatus orderStatus);
    Optional<Order> findByOrderIdAndUser(Long orderId,Users users);
}
