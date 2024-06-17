package ra.project_md4_tqthang.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_md4_tqthang.constants.EHttpStatus;
import ra.project_md4_tqthang.dto.request.PagingRequest;
import ra.project_md4_tqthang.dto.response.ResponseWrapper;
import ra.project_md4_tqthang.exception.CustomException;
import ra.project_md4_tqthang.model.Category;
import ra.project_md4_tqthang.model.Products;
import ra.project_md4_tqthang.model.Role;
import ra.project_md4_tqthang.model.Users;
import ra.project_md4_tqthang.service.impl.CategoryServiceImpl;
import ra.project_md4_tqthang.service.impl.ProductServiceImpl;
import ra.project_md4_tqthang.service.impl.RoleServiceImpl;
import ra.project_md4_tqthang.service.impl.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/admin")
public class AdminController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private ProductServiceImpl productService;

    //ROLE_ADMIN - GET - Tìm kiếm người dùng theo tên    #4905
    @GetMapping("/users/search")
    public ResponseEntity<List<Users>> searchUsers(@RequestParam String fullName) {
        List<Users> users = userService.searchUserByName(fullName);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //ROLE_ADMIN - GET - Lấy về danh sách quyền #4904
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles(){
        List<Role> roleList = roleService.findAll();
        return new ResponseEntity<>(roleList, HttpStatus.OK);
    }

    //ROLE_ADMIN - GET - Khóa / Mở khóa người dùng #4903
    @GetMapping("/users/{userId}")
    public ResponseEntity<Boolean> getUserById(@PathVariable Long userId){
        Users users = userService.searchUserById(userId);
        users.setStatus(!users.getStatus());
        return new ResponseEntity<>(users.getStatus(), HttpStatus.OK);
    }

    //ROLE_ADMIN - GET - Lấy ra danh sách người dùng (phân trang và sắp xếp) #4900
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

    //ROLE_ADMIN - DELETE - Xóa danh mục #4915
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) throws CustomException {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(ResponseEntity.noContent(),HttpStatus.NO_CONTENT);
    }

    //ROLE_ADMIN - PUT - Chỉnh sửa thông tin danh mục #4914
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long categoryId) throws CustomException {
        Category category = categoryService.findCateById(categoryId);
        return new ResponseEntity<>(categoryService.updateCategory(category), HttpStatus.OK);
    }

    //ROLE_ADMIN - POST - Thêm mới danh mục  #4913
    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) throws CustomException {
        return new ResponseEntity<>(categoryService.addCategory(category), HttpStatus.CREATED);
    }

    //ROLE_ADMIN - GET - Lấy về thông tin danh mục theo id #4912
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) throws CustomException {
        Category category = categoryService.findCateById(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    //ROLE_ADMIN - GET - Lấy về danh sách tất cả danh mục (sắp xếp và phân trang)  #4911
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories(@RequestBody PagingRequest pagingRequest){
        List<Category> categories = categoryService.getCategoryByPage(
                pagingRequest.getPage()-1,
                pagingRequest.getPageItem(),
                pagingRequest.getOrderDirection(),
                pagingRequest.getSortBy()
        ).getContent();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    //ROLE_ADMIN - DELETE - Xóa sản phẩm  #4910
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //ROLE_ADMIN - PUT - Chỉnh sửa thông tin sản phẩm #4909
    @PutMapping("/products/{productId}")
    public ResponseEntity<Products> updateProduct(@PathVariable Long productId){
        Products products = productService.getProductInfo(productId);
        return new ResponseEntity<>(productService.updateProduct(products), HttpStatus.OK);
    }

    //ROLE_ADMIN - POST - Thêm mới sản phẩm #4908
    @PostMapping("/products")
    public ResponseEntity<Products> addProduct(@RequestBody Products products){
        return new ResponseEntity<>(productService.addProduct(products), HttpStatus.CREATED);
    }

    //ROLE_ADMIN - GET - Lấy về thông tin sản phẩm theo id #4907
    @GetMapping("/products/{productId}")
    public ResponseEntity<Products> getProductById(@PathVariable Long productId){
        Products products = productService.getProductInfo(productId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //ROLE_ADMIN - GET - Lấy về danh sách tất cả sản phẩm (sắp xếp và phân trang)  #4906
    @GetMapping("/products")
    public ResponseEntity<List<Products>> getAllProducts(@RequestBody PagingRequest pagingRequest){
        List<Products> products = productService.getAllProducts(
                pagingRequest.getPage()-1,
                pagingRequest.getPageItem(),
                pagingRequest.getOrderDirection(),
                pagingRequest.getSortBy()
        ).getContent();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
