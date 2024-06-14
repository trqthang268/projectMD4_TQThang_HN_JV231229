package ra.project_md4_tqthang.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormRegister {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String userName;
    private Set<String> roles;
}
