package ra.project_md4_tqthang.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JWTResponse {
    private String token;
    private String userName;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Boolean status;
    private Date createAt;
    private Date updateAt;
    private Boolean isDeleted;
    private Set<String> roles;
}
