package ra.project_md4_tqthang.service;

import ra.project_md4_tqthang.dto.request.FormLogin;
import ra.project_md4_tqthang.dto.request.FormRegister;
import ra.project_md4_tqthang.dto.response.JWTResponse;

public interface IAuthService {
    void handleRegister(FormRegister formRegister);
    JWTResponse handleLogin(FormLogin formLogin);
}
