package ra.project_md4_tqthang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project_md4_tqthang.model.OrderDetail;
import ra.project_md4_tqthang.model.OrderDetailId;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {
    List<OrderDetail> findAllByOrder_OrderId(Long orderId);
}
