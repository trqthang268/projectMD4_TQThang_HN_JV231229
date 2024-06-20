package ra.project_md4_tqthang.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.project_md4_tqthang.validation.EmailExist;
import ra.project_md4_tqthang.validation.UserExist;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormRegister {
    @NotEmpty(message = "Full name is empty")
    @NotBlank(message = "Full name is blank")
    private String fullName;

    @EmailExist
    @NotEmpty(message = "Email is empty")
    @NotBlank(message = "Email is blank")
    @Pattern(regexp = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$",message = "Invalid email format!")
    private String email;

    @NotEmpty(message = "Password is empty")
    @NotBlank(message = "Password is blank")
    private String password;

    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$",message = "Invalid phone format!")
    @NotEmpty(message = "Phone is empty")
    @NotBlank(message = "Phone is blank")
    private String phone;

    @NotEmpty(message = "Username is empty")
    @NotBlank(message = "Username is blank")
    @UserExist
    private String userName;
    private Set<String> roles;
}
