package ra.project_md4_tqthang.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewAddressRequest {
    @NotEmpty(message = "Address is empty")
    @NotBlank(message = "Address is blank")
    private String fullAddress;
    @NotEmpty(message = "Phone is empty")
    @NotBlank(message = "Phone is blank")
    private String phone;
    @NotEmpty(message = "Receive name is empty")
    @NotBlank(message = "Receive name is blank")
    private String receiveName;
}
