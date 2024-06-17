package ra.project_md4_tqthang.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project_md4_tqthang.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
}
