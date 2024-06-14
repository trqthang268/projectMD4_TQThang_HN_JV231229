package ra.project_md4_tqthang.service.impl;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ICrossReferenceHandler;
import org.springframework.stereotype.Service;
import ra.project_md4_tqthang.constants.RoleName;
import ra.project_md4_tqthang.model.Role;
import ra.project_md4_tqthang.repository.IRoleRepository;
import ra.project_md4_tqthang.service.IRoleService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final IRoleRepository roleRepository;
    @Override
    public Role findByRoleName(RoleName roleName) {
        return roleRepository.findRoleByRoleName(roleName).orElseThrow(()->new NoSuchElementException("Role not found"));
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
