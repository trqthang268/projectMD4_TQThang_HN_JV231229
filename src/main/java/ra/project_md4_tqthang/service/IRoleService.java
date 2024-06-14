package ra.project_md4_tqthang.service;

import ra.project_md4_tqthang.constants.RoleName;
import ra.project_md4_tqthang.model.Role;

import java.util.List;

public interface IRoleService {
    Role findByRoleName(RoleName roleName);
    List<Role> findAll();
}
