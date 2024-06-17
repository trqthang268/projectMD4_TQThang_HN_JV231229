package ra.project_md4_tqthang.security.principal;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ra.project_md4_tqthang.model.Users;
import ra.project_md4_tqthang.repository.IUserRepository;

import java.util.Optional;

@Service
public class UserDetailCustomService implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> optionalUsers = userRepository.findUsersByUserName(username);
        if (optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            return UserDetailCustom.builder()
                    .userId(users.getUserId())
                    .userName(users.getUserName())
                    .email(users.getEmail())
                    .address(users.getAddress())
                    .password(users.getPassword())
                    .createAt(users.getCreateAt())
                    .updateAt(users.getUpdateAt())
                    .fullName(users.getFullName())
                    .status(users.getStatus())
                    .phoneNumber(users.getPhoneNumber())
                    .isDeleted(users.getIsDeleted())
                    .authorities(
                            users.getRoles().stream()
                                    .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                            .toList()
                    ).build();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
