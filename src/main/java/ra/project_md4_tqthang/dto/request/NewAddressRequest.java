package ra.project_md4_tqthang.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewAddressRequest {
    private String fullAddress;

    private String phone;

    private String receiveName;
}
