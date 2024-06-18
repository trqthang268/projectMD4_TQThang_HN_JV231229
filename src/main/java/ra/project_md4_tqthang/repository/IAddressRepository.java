package ra.project_md4_tqthang.repository;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project_md4_tqthang.model.Address;
import ra.project_md4_tqthang.model.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAddressRepository extends JpaRepository<Address,Long> {
    Optional<Address> findByAddressIdAndUser(Long addressId, Users user);
    List<Address> findByUser(Users user);
}
