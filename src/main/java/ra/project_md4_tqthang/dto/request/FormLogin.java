package ra.project_md4_tqthang.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormLogin {
    @NotBlank(message = "username must be not blank")
    @NotEmpty(message = "username must be not empty")
    private String username;
    @NotBlank(message = "password must be not blank")
    @NotEmpty(message = "password must be not empty")
    private String password;
}
