package ra.project_md4_tqthang.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_md4_tqthang.constants.EHttpStatus;
import ra.project_md4_tqthang.dto.request.PagingRequest;
import ra.project_md4_tqthang.dto.response.ResponseWrapper;
import ra.project_md4_tqthang.model.Role;
import ra.project_md4_tqthang.model.Users;
import ra.project_md4_tqthang.service.impl.RoleServiceImpl;
import ra.project_md4_tqthang.service.impl.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    //ROLE_ADMIN - GET - Tìm kiếm người dùng theo tên    @GetMapping("/users/search")
    public ResponseEntity<List<Users>> searchUsers(@RequestParam String fullName) {
        List<Users> users = userService.searchUserByName(fullName);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //ROLE_ADMIN - GET - Lấy về danh sách quyền
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles(){
        List<Role> roleList = roleService.findAll();
        return new ResponseEntity<>(roleList, HttpStatus.OK);
    }

    //ROLE_ADMIN - GET - Khóa / Mở khóa người dùng
    @GetMapping("/users/{userId}")
    public ResponseEntity<Boolean> getUserById(@PathVariable Long userId){
        Users users = userService.searchUserById(userId);
        users.setStatus(!users.getStatus());
        return new ResponseEntity<>(users.getStatus(), HttpStatus.OK);
    }

    //ROLE_ADMIN - GET - Lấy ra danh sách người dùng (phân trang và sắp xếp)
    @GetMapping("/users")
    public ResponseEntity<List<Users>> getUserWithSearchAndPage(@RequestBody PagingRequest pagingRequest){
        List<Users> content = userService.getUsersByPage(
                pagingRequest.getSearchName(),
                pagingRequest.getPage()-1,
                pagingRequest.getPageItem(),
                pagingRequest.getSortBy(),
                pagingRequest.getOrderDirection()
        ).getContent();
        return new ResponseEntity<>(content, HttpStatus.OK);
    }

}
