package ra.project_md4_tqthang.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataError<T> {
    private String message;
    private T data;
    private HttpStatus status;
}
