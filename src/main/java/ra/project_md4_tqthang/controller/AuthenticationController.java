package ra.project_md4_tqthang.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.project_md4_tqthang.constants.EHttpStatus;
import ra.project_md4_tqthang.dto.request.FormLogin;
import ra.project_md4_tqthang.dto.request.FormRegister;
import ra.project_md4_tqthang.dto.response.ResponseWrapper;
import ra.project_md4_tqthang.service.IAuthService;

@RestController
@RequestMapping("/api.myservice.com/v1/auth")

public class AuthenticationController {
    @Autowired
    private IAuthService authService;

    //PermitAll - POST - Đăng kí tài khoản người dùng   #4870
    @PostMapping("/sign-up")
    public ResponseEntity<?> handleRegister(@RequestBody FormRegister formRegister){
        authService.handleRegister(formRegister);
        return new ResponseEntity<>(
                ResponseWrapper.builder().eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.CREATED.value())
                        .data("Register successful")
                        .build()
                ,HttpStatus.CREATED);
    }

    //PermitAll - POST - Đăng nhập tài khoản bằng username và password #4871
    @PostMapping("/sign-in")
    public ResponseEntity<?> handleRegister(@RequestBody FormLogin formLogin){
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(authService.handleLogin(formLogin))
                        .build()
                ,HttpStatus.OK);
    }
}
