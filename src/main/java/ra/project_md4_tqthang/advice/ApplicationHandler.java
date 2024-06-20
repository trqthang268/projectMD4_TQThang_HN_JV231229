package ra.project_md4_tqthang.advice;

import org.hibernate.exception.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ra.project_md4_tqthang.constants.EHttpStatus;
import ra.project_md4_tqthang.dto.response.DataError;
import ra.project_md4_tqthang.dto.response.ResponseWrapper;
import ra.project_md4_tqthang.exception.CustomException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApplicationHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.FAILED)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .data(errors)
                        .build()
        );
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.FAILED)
                        .statusCode(ex.getHttpStatus().value())
                        .data(ex.getMessage())
                        .build(),
                ex.getHttpStatus()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public DataError<String> handleNoSuchElementException(NoSuchElementException ex) {
        return new DataError<>("No Such Element Error", ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataException.class)
    public DataError<?> handUnData(DataException e){
        return new DataError<>("Data Error",e.getLocalizedMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public DataError<?> handleRuntimeException(RuntimeException e){
        return new DataError<>("Runtime Error",e.getLocalizedMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public DataError<?> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e) {
        return new DataError<>("Data Type Not Acceptable",e.getLocalizedMessage(),HttpStatus.BAD_REQUEST);
    }

}
