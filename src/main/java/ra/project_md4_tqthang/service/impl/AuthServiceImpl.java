package ra.project_md4_tqthang.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.project_md4_tqthang.constants.RoleName;
import ra.project_md4_tqthang.dto.request.FormLogin;
import ra.project_md4_tqthang.dto.request.FormRegister;
import ra.project_md4_tqthang.dto.response.JWTResponse;
import ra.project_md4_tqthang.model.Role;
import ra.project_md4_tqthang.model.Users;
import ra.project_md4_tqthang.repository.IUserRepository;
import ra.project_md4_tqthang.security.jwt.JwtProvider;
import ra.project_md4_tqthang.security.principal.UserDetailCustom;
import ra.project_md4_tqthang.service.IAuthService;
import ra.project_md4_tqthang.service.IRoleService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final IRoleService roleService;
    private final IUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void handleRegister(FormRegister formRegister) {
        Set<Role> roles = new HashSet<>();
        if (formRegister.getRoles() == null || formRegister.getRoles().isEmpty()) {
            roles.add(roleService.findByRoleName(RoleName.ROLE_USER));
        } else {
            formRegister.getRoles().forEach(role -> {
                switch (role) {
                    case "admin":
                        roles.add(roleService.findByRoleName(RoleName.ROLE_ADMIN));
                        break;
                    case "user":
                        roles.add(roleService.findByRoleName(RoleName.ROLE_USER));
                        break;
                    case "moderator":
                        roles.add(roleService.findByRoleName(RoleName.ROLE_MODERATOR));
                        break;
                    default:
                        throw new RuntimeException("role not found");
                }
            });
        }
        Users users = Users.builder()
                .fullName(formRegister.getFullName())
                .phoneNumber(formRegister.getPhone())
                .fullName(formRegister.getFullName())
                .email(formRegister.getEmail())
                .password(passwordEncoder.encode(formRegister.getPassword()))
                .roles(roles)
                .status(true)
                .build();
        userRepository.save(users);
    }

    @Override
    public JWTResponse handleLogin(FormLogin formLogin) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                    formLogin.getUsername(),formLogin.getPassword()));
        }catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
        UserDetailCustom userDetailCustom = (UserDetailCustom) authentication.getPrincipal();
        String token = jwtProvider.generateToken(userDetailCustom);
        return JWTResponse.builder()
                .token(token)
                .userName(userDetailCustom.getUsername())
                .address(userDetailCustom.getAddress())
                .fullName(userDetailCustom.getFullName())
                .email(userDetailCustom.getEmail())
                .phoneNumber(userDetailCustom.getPhoneNumber())
                .status(userDetailCustom.getStatus())
                .createAt(userDetailCustom.getCreateAt())
                .updateAt(userDetailCustom.getUpdateAt())
                .isDeleted(userDetailCustom.getIsDeleted())
                .roles(userDetailCustom.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .build();
    }
}
