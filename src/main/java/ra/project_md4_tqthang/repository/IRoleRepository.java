package ra.project_md4_tqthang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project_md4_tqthang.constants.RoleName;
import ra.project_md4_tqthang.model.Role;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByRoleName(RoleName roleName);
}
