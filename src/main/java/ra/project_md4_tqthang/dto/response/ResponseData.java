package ra.project_md4_tqthang.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseData<T>{
    private T data;
    private String message;
    private HttpStatus status;
}
