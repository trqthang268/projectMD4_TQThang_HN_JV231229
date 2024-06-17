package ra.project_md4_tqthang.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CheckoutRequest {
    private String receiveName;
    private String receiveAddress;
    private String receivePhone;
}
