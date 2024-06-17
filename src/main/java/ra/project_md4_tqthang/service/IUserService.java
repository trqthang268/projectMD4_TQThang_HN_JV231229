package ra.project_md4_tqthang.service;

import org.springframework.data.domain.Page;
import ra.project_md4_tqthang.dto.request.ChangePasswordRequest;
import ra.project_md4_tqthang.dto.request.UpdateUserRequest;
import ra.project_md4_tqthang.exception.CustomException;
import ra.project_md4_tqthang.model.Users;

import java.util.List;

public interface IUserService {
    List<Users> searchUserByName(String fullName);
    Users searchUserById(Long id);
    Page<Users> getUsersByPage(String searchName, Integer page, Integer pageItem, String orderBy, String orderDirection);
    void updateUser(Long userId, UpdateUserRequest updateUserRequest);
    void changePassword(Long userId, ChangePasswordRequest changePasswordRequest) throws CustomException;

}
